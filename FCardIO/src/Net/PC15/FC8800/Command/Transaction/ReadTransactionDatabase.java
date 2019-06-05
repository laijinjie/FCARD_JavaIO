/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Net.PC15.FC8800.Command.Transaction;

import Net.PC15.Connector.INConnectorEvent;
import Net.PC15.Data.AbstractTransaction;
import Net.PC15.FC8800.Command.Data.AlarmTransaction;
import Net.PC15.FC8800.Command.Data.ButtonTransaction;
import Net.PC15.FC8800.Command.Data.CardTransaction;
import Net.PC15.FC8800.Command.Data.DoorSensorTransaction;
import Net.PC15.FC8800.Command.Data.SoftwareTransaction;
import Net.PC15.FC8800.Command.Data.SystemTransaction;
import Net.PC15.FC8800.Command.Data.TransactionDatabaseDetail;
import Net.PC15.FC8800.Command.Data.TransactionDetail;
import Net.PC15.FC8800.Command.FC8800Command;
import Net.PC15.FC8800.Command.Transaction.Parameter.ReadTransactionDatabase_Parameter;
import Net.PC15.FC8800.Command.Transaction.Result.ReadTransactionDatabase_Result;
import Net.PC15.FC8800.Packet.FC8800PacketModel;
import Net.PC15.Util.ByteUtil;
import io.netty.buffer.ByteBuf;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * 读取新记录<br/>
 * 读指定类型的记录数据库最新记录，并读取指定数量。<br/>
 * 成功返回结果参考 {@link ReadTransactionDatabase_Result}
 *
 * @author 赖金杰
 */
public class ReadTransactionDatabase extends FC8800Command {

    protected ConcurrentLinkedQueue<ByteBuf> mBufs;
    /**
     * 本次读取的数量
     */
    protected int mReadQuantity;
    protected int mStep;
    protected ReadTransactionDatabase_Parameter thisParameter;
    protected TransactionDetail transactionDetail;
    /**
     * 可读取的新记录数量
     */
    protected int mReadable;
    /**
     * 读取计数
     */
    protected int mReadTotal;

    public ReadTransactionDatabase(ReadTransactionDatabase_Parameter par) {
        _Parameter = par;
        thisParameter = par;
        mStep = 1;

        mReadQuantity = thisParameter.PacketSize;
        if (mReadQuantity == 0 || mReadQuantity > 300 || mReadQuantity < 0) {
            mReadQuantity = 200;
        }
        thisParameter.PacketSize = mReadQuantity;

        //生成返回值结果集
        ReadTransactionDatabase_Result result = new ReadTransactionDatabase_Result();
        result.DatabaseType = thisParameter.DatabaseType;
        _Result = result;

        CreatePacket(8, 1);
    }

    @Override
    protected boolean _CommandStep(INConnectorEvent oEvent, FC8800PacketModel model) {
        switch (mStep) {
            case 1://读取记录数据库空间信息
                return CheckDataBaseDetail(oEvent, model);
            case 2://读取记录
                return CheckReadTransactionResponse(oEvent, model);
            case 3://写记录读索引号
                return CheckWriteReadIndexResponse(oEvent, model);
        }
        return false;
    }

    protected boolean CheckDataBaseDetail(INConnectorEvent oEvent, FC8800PacketModel model) {
        if (CheckResponse_Cmd(model, 8, 1, 0, 0xD * 6)) {
            ByteBuf buf = model.GetDatabuff();

            //解析记录信息
            TransactionDatabaseDetail DatabaseDetail = new TransactionDatabaseDetail();
            DatabaseDetail.SetBytes(buf);
            switch (thisParameter.DatabaseType) {
                case OnCardTransaction:
                    transactionDetail = DatabaseDetail.CardTransactionDetail;
                    break;
                case OnButtonTransaction:
                    transactionDetail = DatabaseDetail.ButtonTransactionDetail;
                    break;
                case OnDoorSensorTransaction:
                    transactionDetail = DatabaseDetail.DoorSensorTransactionDetail;
                    break;
                case OnSoftwareTransaction:
                    transactionDetail = DatabaseDetail.SoftwareTransactionDetail;
                    break;
                case OnAlarmTransaction:
                    transactionDetail = DatabaseDetail.AlarmTransactionDetail;
                    break;
                case OnSystemTransaction:
                    transactionDetail = DatabaseDetail.SystemTransactionDetail;
                    break;
            }
            if (transactionDetail.readable() == 0) {
                RaiseCommandCompleteEvent(oEvent); //没有新记录
                return true;
            } else {
                mStep = 2;
                mBufs = new ConcurrentLinkedQueue<ByteBuf>();

                //更新读取数量
                mReadable = (int) transactionDetail.readable();
                if (thisParameter.Quantity > 0) {
                    if (thisParameter.Quantity < mReadable) {
                        mReadable = thisParameter.Quantity;
                    }
                }
                mReadQuantity = 0;
                mReadTotal = 0;
                _ProcessMax = mReadable;
                _ProcessStep = 0;
                //如果发现记录已循环存储，则强制读索引号=写索引号，这样读取的下一条就是最新早的记录。
                if (transactionDetail.IsCircle) {
                    transactionDetail.ReadIndex = transactionDetail.WriteIndex;
                }
                ReadTransactionNext();
            }
        } else {
            return false;
        }
        return true;
    }

    /**
     * 读取下一包记录
     */
    private void ReadTransactionNext() {
        _ProcessStep = mReadTotal;
        mReadable -= mReadQuantity;
        if (mReadable <= 0) {
            //记录读取完毕，需要更新读索引（更新记录尾号）
            //写读索引
            WriteTransactionReadIndex();
            return;
        }

        //计算本次读取的数量
        mReadQuantity = thisParameter.PacketSize;

        //如果发现读索引号定位在记录末尾，则强制转移到记录头
        if (transactionDetail.ReadIndex == transactionDetail.DataBaseMaxSize) {
            transactionDetail.ReadIndex = 0;
        }
        if (mReadQuantity > mReadable) {
            mReadQuantity = mReadable;
        }

        int iBeginIndex = (int) transactionDetail.ReadIndex + 1;
        int iEndIndex = iBeginIndex + mReadQuantity - 1;

        if (iEndIndex > transactionDetail.DataBaseMaxSize) {
            mReadQuantity = (int) (transactionDetail.DataBaseMaxSize - transactionDetail.ReadIndex);
            iEndIndex = iBeginIndex + mReadQuantity - 1;
        }
        transactionDetail.ReadIndex = iEndIndex;//更新记录尾号

        ByteBuf dataBuf = ByteUtil.ALLOCATOR.buffer(9);
        dataBuf.writeByte(thisParameter.DatabaseType.getValue());
        dataBuf.writeInt(iBeginIndex);
        dataBuf.writeInt(mReadQuantity);

        CreatePacket(8, 4, 0, 9, dataBuf);
        CommandReady();
    }

    /**
     * 检查读取记录的响应
     *
     * @param model
     * @return
     */
    protected boolean CheckReadTransactionResponse(INConnectorEvent oEvent, FC8800PacketModel model) {
        if (CheckResponse_Cmd(model, 8, 4, 0)) {
            try {
                ByteBuf buf = model.GetDatabuff();
                int iSize = buf.getInt(0);
                mReadTotal += iSize;
                _ProcessStep = mReadTotal;
                RaiseCommandProcessEvent(oEvent);

                buf.retain();
                mBufs.add(buf);

                //让命令持续等待下去
                CommandWaitResponse();
            } catch (Exception e) {
                System.out.println("CheckReadTransactionResponse -- 发生错误：" + e.getLocalizedMessage());
            }

            return true;

        } else if (CheckResponse_Cmd(model, 8, 4, 0xFF, 4)) {
            //继续发送下一波
            ReadTransactionNext();
            return true;
        } else {
            return false;
        }
    }

    /**
     * 修改记录读索引号
     */
    private void WriteTransactionReadIndex() {
        ByteBuf dataBuf = ByteUtil.ALLOCATOR.buffer(6);
        dataBuf.writeByte(thisParameter.DatabaseType.getValue());
        dataBuf.writeInt((int) transactionDetail.ReadIndex);
        dataBuf.writeBoolean(false);

        CreatePacket(8, 3, 0, 6, dataBuf);
        mStep = 3;
        CommandReady();
    }

    /**
     * 检查修改记录读索引号的返回值
     *
     * @param oEvent
     * @param model
     * @return
     */
    protected boolean CheckWriteReadIndexResponse(INConnectorEvent oEvent, FC8800PacketModel model) {
        if (CheckResponseOK(model)) {
            ReadTransactionDatabase_Result result = (ReadTransactionDatabase_Result) _Result;
            result.Quantity = mReadTotal;
            result.readable = (int) transactionDetail.readable();

            //命令完结
            CommandOver();

            //Calendar begintime = Calendar.getInstance();
            //开始拆分接收到的数据包
            Analysis(mReadTotal);
            //Calendar endtime = Calendar.getInstance();
            //long waitTime = endtime.getTimeInMillis() - begintime.getTimeInMillis();
            //System.out.println("解析数据包耗时：" + waitTime);

            //拆分后返回事件
            RaiseCommandCompleteEvent(oEvent);
            return true;
        }
        return false;

    }

    @Override
    protected void Release0() {
        _Parameter = null;
        _Result = null;
        thisParameter = null;
        ClearBuf();
        return;
    }

    /**
     * 分析缓冲中的数据包
     */
    private void Analysis(int iSize) {
        ReadTransactionDatabase_Result result = (ReadTransactionDatabase_Result) _Result;
        result.Quantity = iSize;

        ArrayList<AbstractTransaction> trList = new ArrayList<>(iSize);
        result.TransactionList = trList;

        Class TransactionType;
        switch (result.DatabaseType) {
            case OnCardTransaction:
                TransactionType = CardTransaction.class;
                break;
            case OnButtonTransaction:
                TransactionType = ButtonTransaction.class;
                break;
            case OnDoorSensorTransaction:
                TransactionType = DoorSensorTransaction.class;
                break;
            case OnSoftwareTransaction:
                TransactionType = SoftwareTransaction.class;
                break;
            case OnAlarmTransaction:
                TransactionType = AlarmTransaction.class;
                break;
            case OnSystemTransaction:
                TransactionType = SystemTransaction.class;
                break;
            default:
                result.Quantity = 0;
                return;

        }

        while (mBufs.peek() != null) {
            ByteBuf buf = mBufs.poll();
            iSize = buf.readInt();

            for (int i = 0; i < iSize; i++) {
                try {
                    AbstractTransaction cd = (AbstractTransaction) TransactionType.newInstance();
                    cd.SerialNumber = buf.readInt();
                    cd.SetBytes(buf);
                    trList.add(cd);
                } catch (Exception e) {
                    result.Quantity = 0;
                    return;
                }

            }
            buf.release();
        }

    }

    protected void ClearBuf() {
        if (mBufs == null) {
            return;
        }
        while (mBufs.peek() != null) {
            ByteBuf buf = mBufs.poll();
            buf.release();
        }
    }
}

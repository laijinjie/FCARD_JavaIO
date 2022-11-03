package Face.Transaction;

import Door.Access.Connector.INConnectorEvent;
import Door.Access.Data.AbstractTransaction;

import Door.Access.Door8800.Command.Data.TransactionDetail;
import Door.Access.Door8800.Command.Door8800Command;
import Door.Access.Door8800.Packet.Door8800PacketModel;
import Door.Access.Util.ByteUtil;
import Face.Data.*;
import Face.Transaction.Parameter.ReadTransactionDatabase_Parameter;
import Face.Transaction.Result.ReadTransactionDatabase_Result;
import io.netty.buffer.ByteBuf;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * 读取新记录
 *
 * @author F
 */
public class ReadTransactionDatabase extends Door8800Command {

    /**
     * 数据包队列
     */
    protected ConcurrentLinkedQueue<ByteBuf> mBufs;
    /**
     * 读取数量
     */
    protected int mReadQuantity;
    /**
     * 步骤
     */
    protected int mStep;
    /**
     * 读取记录参数
     */
    protected ReadTransactionDatabase_Parameter thisParameter;
    /**
     * 记录详情
     */
    protected TransactionDetail transactionDetail;
    /**
     * 可读取的新记录数量
     */
    protected int mReadable;
    /**
     * 读取计数
     */
    protected int mReadTotal;

    /**
     * 读取新记录
     *
     * @param par 读取新记录参数
     */
    public ReadTransactionDatabase(ReadTransactionDatabase_Parameter par) {
        _Parameter = par;
        thisParameter = par;
        mStep = 1;

        mReadQuantity = 60;

        thisParameter.PacketSize = mReadQuantity;

        //生成返回值结果集
        ReadTransactionDatabase_Result result = new ReadTransactionDatabase_Result();
        result.DatabaseType = thisParameter.DatabaseType;
        _Result = result;

        CreatePacket(8, 1);
    }

    @Override
    protected boolean _CommandStep(INConnectorEvent oEvent, Door8800PacketModel model) {
        switch (mStep) {
            //读取记录数据库空间信息
            case 1:
                return CheckDataBaseDetail(oEvent, model);
            //读取记录
            case 2:
                return CheckReadTransactionResponse(oEvent, model);
            //写记录读索引号
            case 3:
                return CheckWriteReadIndexResponse(oEvent, model);
            default:
        }
        return false;
    }

    /**
     * 检查记录类型
     *
     * @param oEvent 事件
     * @param model 数据
     * @return
     */
    protected boolean CheckDataBaseDetail(INConnectorEvent oEvent, Door8800PacketModel model) {
        if (CheckResponse_Cmd(model, 8, 1)) {
            ByteBuf buf = model.GetDatabuff();

            //解析记录信息
            TransactionDatabaseDetail DatabaseDetail = new TransactionDatabaseDetail();
            DatabaseDetail.SetBytes(buf);
            switch (thisParameter.DatabaseType) {
                case OnCardTransaction:
                    transactionDetail = DatabaseDetail.CardTransactionDetail;
                    break;
                case OnDoorSensorTransaction:
                    transactionDetail = DatabaseDetail.DoorSensorTransactionDetail;
                    break;
                case OnSystemTransaction:
                    transactionDetail = DatabaseDetail.SystemTransactionDetail;
                    break;
                case OnBodyTemperatureTransaction:
                    transactionDetail = DatabaseDetail.BodyTemperatureTransactionDetail;
                    break;
                default:
            }
            if (transactionDetail.readable() == 0) {
                RaiseCommandCompleteEvent(oEvent);
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
        //更新记录尾号
        transactionDetail.ReadIndex = iEndIndex;

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
     * @param oEvent 事件
     * @param model 数据
     * @return
     */
    protected boolean CheckReadTransactionResponse(INConnectorEvent oEvent, Door8800PacketModel model) {
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
    protected boolean CheckWriteReadIndexResponse(INConnectorEvent oEvent, Door8800PacketModel model) {
        if (CheckResponseOK(model)) {
            ReadTransactionDatabase_Result result = (ReadTransactionDatabase_Result) _Result;
            result.Quantity = mReadTotal;
            result.readable = (int) transactionDetail.readable();

            //命令完结
            CommandOver();

            //开始拆分接收到的数据包
            try {
                Analysis(mReadTotal);
            } catch (Exception e) {

            }
            //拆分后返回事件
            RaiseCommandCompleteEvent(oEvent);
            return true;
        }
        return false;

    }

    /**
     * 分析缓冲中的数据包
     *
     * @param iSize 数据包大小
     * @throws Exception 错误
     */
    protected void Analysis(int iSize) throws Exception {
        ReadTransactionDatabase_Result result = (ReadTransactionDatabase_Result) _Result;
        result.Quantity = iSize;
        ArrayList<AbstractTransaction> trList = new ArrayList<AbstractTransaction>(iSize);
        result.TransactionList = trList;

        Class TransactionType;
        switch (result.DatabaseType) {
            case OnCardTransaction:
                TransactionType = CardTransaction.class;
                break;
            case OnDoorSensorTransaction:
                TransactionType = DoorSensorTransaction.class;
                break;
            case OnSystemTransaction:
                TransactionType = SystemTransaction.class;
                break;
            case OnBodyTemperatureTransaction:
                TransactionType = BodyTemperatureTransaction.class;
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
                    AbstractTransaction cd = (AbstractTransaction) TransactionType.getDeclaredConstructor().newInstance();
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

    /**
     * 清空数据包
     */
    protected void ClearBuf() {
        if (mBufs == null) {
            return;
        }
        while (mBufs.peek() != null) {
            ByteBuf buf = mBufs.poll();
            buf.release();
        }
    }

    @Override
    protected void Release0() {
        _Parameter = null;
        _Result = null;
        thisParameter = null;
        ClearBuf();
    }
}

package Face.Transaction;

import Door.Access.Connector.INConnectorEvent;
import Door.Access.Data.AbstractTransaction;

import Door.Access.Door8800.Command.Door8800Command;
import Door.Access.Door8800.Packet.Door8800PacketModel;
import Face.Data.BodyTemperatureTransaction;
import Face.Data.CardTransaction;
import Face.Data.DoorSensorTransaction;
import Face.Data.SystemTransaction;
import Face.Transaction.Parameter.ReadTransactionDatabaseByIndex_Parameter;
import Face.Transaction.Result.ReadTransactionDatabaseByIndex_Result;
import io.netty.buffer.ByteBuf;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * 按照索引读取数据库记录信息
 */
public class ReadTransactionDatabaseByIndex extends Door8800Command {

    /**
     * 数据包队列
     */
    protected ConcurrentLinkedQueue<ByteBuf> mBuffs;
    private int mQuantity;

    /**
     * 读取数据库记录信息
     *
     * @param parameter
     */
    public ReadTransactionDatabaseByIndex(ReadTransactionDatabaseByIndex_Parameter parameter) {
        _Parameter = parameter;
        CreatePacket(0x08, 0x04, 0x00, parameter.getDataLen(), parameter.getBytes());
        mBuffs = new ConcurrentLinkedQueue<ByteBuf>();
        mQuantity = parameter.Quantity;
        _ProcessMax = mQuantity;
    }

    @Override
    protected boolean _CommandStep(INConnectorEvent oEvent, Door8800PacketModel model) {
        if (CheckResponse_Cmd(model, 8, 4, 0)) {
            try {
                ByteBuf buf = model.GetDatabuff();
                int iSize = buf.getInt(0);
                _ProcessStep += iSize;
                RaiseCommandProcessEvent(oEvent);

                buf.retain();
                mBuffs.add(buf);

                //让命令持续等待下去
                CommandWaitResponse();
            } catch (Exception e) {
                System.out.println("发送错误：" + e.getLocalizedMessage());
            }

            return true;

        } else if (CheckResponse_Cmd(model, 8, 4, 0xFF, 4)) {
            ByteBuf buf = model.GetDatabuff();
            int iSize = buf.readInt();
            ReadTransactionDatabaseByIndex_Result result = new ReadTransactionDatabaseByIndex_Result();
            ReadTransactionDatabaseByIndex_Parameter par = (ReadTransactionDatabaseByIndex_Parameter) _Parameter;
            result.DatabaseType = par.DatabaseType;
            result.ReadIndex = par.ReadIndex;
            result.Quantity = iSize;
            _Result = result;

            if (iSize > 0) {
                //开始拆分接收到的数据包
                try {
                    Analysis(iSize);
                } catch (Exception e) {

                }
                //拆分后返回事件
                RaiseCommandCompleteEvent(oEvent);
            } else {
                //返回没有收到数据
                RaiseCommandCompleteEvent(oEvent);
            }
            return true;
        } else {
            return false;
        }
    }

    /**
     * 解析数据包
     *
     * @param iSize
     * @throws Exception
     */
    protected void Analysis(int iSize) throws Exception {
        ReadTransactionDatabaseByIndex_Result result = (ReadTransactionDatabaseByIndex_Result) _Result;
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

        while (mBuffs.peek() != null) {
            ByteBuf buf = mBuffs.poll();
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

    @Override
    protected void Release0() {

    }
}

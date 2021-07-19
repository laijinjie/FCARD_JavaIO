/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Door.Access.Door89H.Command.Transaction;

import Door.Access.Connector.INConnectorEvent;
import Door.Access.Data.AbstractTransaction;
import Door.Access.Door8800.Command.Data.AlarmTransaction;
import Door.Access.Door8800.Command.Data.ButtonTransaction;
import Door.Access.Door89H.Command.Data.CardTransaction;
import Door.Access.Door8800.Command.Data.DoorSensorTransaction;
import Door.Access.Door8800.Command.Data.SoftwareTransaction;
import Door.Access.Door8800.Command.Data.SystemTransaction;
import Door.Access.Door8800.Command.Transaction.Parameter.ReadTransactionDatabaseByIndex_Parameter;
import Door.Access.Door8800.Command.Transaction.Result.ReadTransactionDatabaseByIndex_Result;
import Door.Access.Door8800.Packet.Door8800PacketModel;
import io.netty.buffer.ByteBuf;
import java.util.ArrayList;

/**
 * 针对Door89H使用，读记录数据库<br>
 * 按指定索引号开始读指定类型的记录数据库，并读取指定数量。<br>
 * 成功返回结果参考 {@link ReadTransactionDatabaseByIndex_Result}
 *
 * @author 徐铭康
 */
public class ReadTransactionDatabaseByIndex extends Door.Access.Door8800.Command.Transaction.ReadTransactionDatabaseByIndex {
    
    public ReadTransactionDatabaseByIndex(ReadTransactionDatabaseByIndex_Parameter par) {
        super(par);
    }
    
    @Override
    protected void Analysis(int iSize)  throws Exception{
        ReadTransactionDatabaseByIndex_Result result = (ReadTransactionDatabaseByIndex_Result) _Result;
        result.Quantity = iSize;

        ArrayList<AbstractTransaction> trList = new ArrayList<>(iSize);
        result.TransactionList = trList;

        Class TransactionType;
        switch (result.DatabaseType) {
            case OnCardTransaction:
                TransactionType = Door.Access.Door89H.Command.Data.CardTransaction.class;
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
                    AbstractTransaction cd = (AbstractTransaction)TransactionType.newInstance();
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
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Net.PC15.FC89H.Command.Transaction;

import Net.PC15.Connector.INConnectorEvent;
import Net.PC15.Data.AbstractTransaction;
import Net.PC15.FC8800.Command.Data.AlarmTransaction;
import Net.PC15.FC8800.Command.Data.ButtonTransaction;
import Net.PC15.FC89H.Command.Data.CardTransaction;
import Net.PC15.FC8800.Command.Data.DoorSensorTransaction;
import Net.PC15.FC8800.Command.Data.SoftwareTransaction;
import Net.PC15.FC8800.Command.Data.SystemTransaction;
import Net.PC15.FC8800.Command.Transaction.Parameter.ReadTransactionDatabaseByIndex_Parameter;
import Net.PC15.FC8800.Command.Transaction.Result.ReadTransactionDatabaseByIndex_Result;
import Net.PC15.FC8800.Packet.FC8800PacketModel;
import io.netty.buffer.ByteBuf;
import java.util.ArrayList;

/**
 * 针对FC89H使用，读记录数据库<br/>
 * 按指定索引号开始读指定类型的记录数据库，并读取指定数量。<br/>
 * 成功返回结果参考 {@link ReadTransactionDatabaseByIndex_Result}
 *
 * @author 徐铭康
 */
public class ReadTransactionDatabaseByIndex extends Net.PC15.FC8800.Command.Transaction.ReadTransactionDatabaseByIndex {
    
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
                TransactionType = Net.PC15.FC89H.Command.Data.CardTransaction.class;
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

            if ((buf.capacity() - 4) % 37 == 0) {
                 for (int i = 0; i < iSize; i++) {
                    try {
                        AbstractTransaction cd = (CardTransaction) TransactionType.newInstance();
                        cd.SerialNumber = buf.readInt();
                        cd.SetBytes(buf);
                        trList.add(cd);
                    } catch (Exception e) {
                        result.Quantity = 0;
                        return;
                    }

                }
                 
             }
            else {
                buf.release();
                 throw new Exception("数据流长度不正确");
            }
            
            buf.release();
        }

    }
}

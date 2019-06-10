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
import Net.PC15.FC8800.Command.Data.CardTransaction;
import Net.PC15.FC8800.Command.Data.DoorSensorTransaction;
import Net.PC15.FC8800.Command.Data.SoftwareTransaction;
import Net.PC15.FC8800.Command.Data.SystemTransaction;
import Net.PC15.FC8800.Command.Transaction.Parameter.ReadTransactionDatabase_Parameter;
import Net.PC15.FC8800.Command.Transaction.Result.ReadTransactionDatabase_Result;
import Net.PC15.FC8800.Packet.FC8800PacketModel;
import io.netty.buffer.ByteBuf;
import java.util.ArrayList;

/**
 * 针对FC89H使用，读取新记录<br/>
 * 读指定类型的记录数据库最新记录，并读取指定数量。<br/>
 * 成功返回结果参考 {@link ReadTransactionDatabase_Result}
 *
 * @author 徐铭康
 */
public class ReadTransactionDatabase extends Net.PC15.FC8800.Command.Transaction.ReadTransactionDatabase {
    
    public ReadTransactionDatabase(ReadTransactionDatabase_Parameter par) {
        super(par);
    }
    /**
     * 分析缓冲中的数据包
     */
    @Override
    protected void Analysis(int iSize)  throws Exception{
        ReadTransactionDatabase_Result result = (ReadTransactionDatabase_Result) _Result;
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

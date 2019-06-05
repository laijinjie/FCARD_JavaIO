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
    protected boolean _CommandStep(INConnectorEvent oEvent, FC8800PacketModel model) {
        if (CheckResponse_Cmd(model, 8, 4, 0)) {
            try {
                ByteBuf buf = model.GetDatabuff();
                int iSize = buf.getInt(0);
                _ProcessStep += iSize;
                RaiseCommandProcessEvent(oEvent);

                buf.retain();
                mBufs.add(buf);

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
                Analysis(iSize);

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
     * 分析缓冲中的数据包
     */
    private void Analysis(int iSize) {
        ReadTransactionDatabaseByIndex_Result result = (ReadTransactionDatabaseByIndex_Result) _Result;
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
                    AbstractTransaction cd = (Net.PC15.FC89H.Command.Data.CardTransaction) TransactionType.newInstance();
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

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Net.PC15.FC8800.Command.Transaction;

import Net.PC15.Command.CommandParameter;
import Net.PC15.Connector.INConnectorEvent;
import Net.PC15.FC8800.Command.Transaction.Result.ReadTransactionDatabaseDetail_Result;
import Net.PC15.FC8800.Command.FC8800Command;
import Net.PC15.FC8800.Packet.FC8800PacketModel;
import io.netty.buffer.ByteBuf;

/**
 * 读取控制器中的记录数据库信息<br/>
 * 成功返回结果参考 {@link ReadTransactionDatabaseDetail_Result}
 *
 * @author 赖金杰
 */
public class ReadTransactionDatabaseDetail extends FC8800Command {

    public ReadTransactionDatabaseDetail(CommandParameter par) {
        _Parameter = par;
        CreatePacket(8, 1);
    }

    @Override
    protected void Release0() {
        return;
    }

    @Override
    protected boolean _CommandStep(INConnectorEvent oEvent, FC8800PacketModel model) {
        if (CheckResponse_Cmd(model, 8, 1, 0, 0xD * 6)) {
            ByteBuf buf = model.GetDatabuff();

            ReadTransactionDatabaseDetail_Result r = new ReadTransactionDatabaseDetail_Result();
            r.DatabaseDetail.SetBytes(buf);

            //设定返回值
            _Result = r;

            RaiseCommandCompleteEvent(oEvent);
            return true;
        } else {
            return false;
        }
    }
}

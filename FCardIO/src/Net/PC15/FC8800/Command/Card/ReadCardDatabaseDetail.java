/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Net.PC15.FC8800.Command.Card;

import Net.PC15.Command.CommandParameter;
import Net.PC15.Connector.INConnectorEvent;
import Net.PC15.FC8800.Command.Card.Result.ReadCardDatabaseDetail_Result;
import Net.PC15.FC8800.Command.FC8800Command;
import Net.PC15.FC8800.Packet.FC8800PacketModel;
import io.netty.buffer.ByteBuf;

/**
 * 读取控制器中的卡片数据库信息<br/>
 * 成功返回结果参考 {@link ReadCardDatabaseDetail_Result}
 *
 * @author 赖金杰
 */
public class ReadCardDatabaseDetail extends FC8800Command {

    public ReadCardDatabaseDetail(CommandParameter par) {
        _Parameter = par;
        CreatePacket(7, 1);
    }

    @Override
    protected void Release0() {
        return;
    }

    @Override
    protected boolean _CommandStep(INConnectorEvent oEvent, FC8800PacketModel model) {
        if (CheckResponse_Cmd(model, 7, 1, 0, 0x10)) {
            ByteBuf buf = model.GetDatabuff();

            ReadCardDatabaseDetail_Result r = new ReadCardDatabaseDetail_Result();
            r.SortDataBaseSize = buf.readUnsignedInt();
            r.SortCardSize = buf.readUnsignedInt();
            r.SequenceDataBaseSize = buf.readUnsignedInt();
            r.SequenceCardSize = buf.readUnsignedInt();            
            //设定返回值
            _Result = r;

            RaiseCommandCompleteEvent(oEvent);
            return true;
        } else {
            return false;
        }
    }

}

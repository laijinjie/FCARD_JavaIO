/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Door.Access.Door8800.Command.System;

import Door.Access.Command.CommandParameter;
import Door.Access.Connector.INConnectorEvent;
import Door.Access.Door8800.Command.Door8800Command;
import Door.Access.Door8800.Command.System.Result.ReadDeadline_Result;
import Door.Access.Door8800.Packet.Door8800PacketModel;
import io.netty.buffer.ByteBuf;

/**
 * 读取控制器有效期剩余天数
 * <p>
 * 成功返回结果参考 {@link ReadDeadline_Result}
 *
 * @author 赖金杰
 */
public class ReadDeadline extends Door8800Command {

    public ReadDeadline(CommandParameter par) {
        _Parameter = par;

        CreatePacket(1, 7);
    }

    @Override
    protected void Release0() {
        return;
    }

    @Override
    protected boolean _CommandStep(INConnectorEvent oEvent, Door8800PacketModel model) {
        if (CheckResponse_Cmd(model, 1, 7, 0, 2)) {
            ByteBuf buf = model.GetDatabuff();

            //设定返回值
            _Result = new ReadDeadline_Result(buf.readUnsignedShort());

            RaiseCommandCompleteEvent(oEvent);
            return true;
        } else {
            return false;
        }
    }

}

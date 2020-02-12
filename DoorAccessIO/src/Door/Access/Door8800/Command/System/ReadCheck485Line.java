/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Door.Access.Door8800.Command.System;

import Door.Access.Command.CommandParameter;
import Door.Access.Connector.INConnectorEvent;
import Door.Access.Door8800.Command.Door8800Command;
import Door.Access.Door8800.Command.System.Result.ReadCheck485Line_Result;
import Door.Access.Door8800.Packet.Door8800PacketModel;
import io.netty.buffer.ByteBuf;

/**
 * 485线路反接检测开关.<br/>
 * 成功返回结果参考 {@link ReadCheck485Line_Result}
 *
 * @author 赖金杰
 */
public class ReadCheck485Line extends Door8800Command {

    public ReadCheck485Line(CommandParameter par) {
        _Parameter = par;
        CreatePacket(1, 0x13, 1);
    }

    @Override
    protected void Release0() {
        return;
    }

    @Override
    protected boolean _CommandStep(INConnectorEvent oEvent, Door8800PacketModel model) {
        if (CheckResponse_Cmd(model, 1, 0x13, 1, 1)) {
            ByteBuf buf = model.GetDatabuff();
            //设定返回值
            ReadCheck485Line_Result ret = new ReadCheck485Line_Result();
            _Result = ret;
            int v = buf.readUnsignedByte();
            ret.Use = v == 1;

            RaiseCommandCompleteEvent(oEvent);
            return true;
        } else {
            return false;
        }
    }

}

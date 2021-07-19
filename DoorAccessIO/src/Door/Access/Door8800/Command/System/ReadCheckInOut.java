/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Door.Access.Door8800.Command.System;

import Door.Access.Command.CommandParameter;
import Door.Access.Connector.INConnectorEvent;
import Door.Access.Door8800.Command.Door8800Command;
import Door.Access.Door8800.Command.System.Result.ReadCheckInOut_Result;
import Door.Access.Door8800.Packet.Door8800PacketModel;
import io.netty.buffer.ByteBuf;

/**
 * 防潜回参数.<br>
 * 成功返回结果参考 {@link ReadCheckInOut_Result}
 *
 * @author 赖金杰
 */
public class ReadCheckInOut extends Door8800Command {

    public ReadCheckInOut(CommandParameter par) {
        _Parameter = par;
        CreatePacket(1, 0xA, 0x8F);
    }

    @Override
    protected void Release0() {
        return;
    }

    @Override
    protected boolean _CommandStep(INConnectorEvent oEvent, Door8800PacketModel model) {
        if (CheckResponse_Cmd(model, 1, 0xA, 0x8F, 1)) {
            ByteBuf buf = model.GetDatabuff();
            //设定返回值
            ReadCheckInOut_Result ret = new ReadCheckInOut_Result();
            _Result = ret;

            ret.Mode = buf.readUnsignedByte();
            RaiseCommandCompleteEvent(oEvent);
            return true;
        } else {
            return false;
        }
    }

}

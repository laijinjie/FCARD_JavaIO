/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Door.Access.Door8800.Command.System;

import Door.Access.Command.CommandParameter;
import Door.Access.Connector.INConnectorEvent;
import Door.Access.Door8800.Command.Door8800Command;
import Door.Access.Door8800.Command.System.Result.ReadCardDeadlineTipDay_Result;
import Door.Access.Door8800.Packet.Door8800PacketModel;
import io.netty.buffer.ByteBuf;

/**
 * 有效期即将过期提醒时间.<br/>
 * 成功返回结果参考 {@link ReadCardDeadlineTipDay_Result}
 *
 * @author 赖金杰
 */
public class ReadCardDeadlineTipDay extends Door8800Command {

    public ReadCardDeadlineTipDay(CommandParameter par) {
        _Parameter = par;
        CreatePacket(1, 0x15, 1);
    }

    @Override
    protected void Release0() {
        return;
    }

    @Override
    protected boolean _CommandStep(INConnectorEvent oEvent, Door8800PacketModel model) {
        if (CheckResponse_Cmd(model, 1, 0x15, 1, 1)) {
            ByteBuf buf = model.GetDatabuff();
            //设定返回值
            ReadCardDeadlineTipDay_Result ret = new ReadCardDeadlineTipDay_Result();
            _Result = ret;
            ret.Day = buf.readUnsignedByte();

            RaiseCommandCompleteEvent(oEvent);
            return true;
        } else {
            return false;
        }
    }

}

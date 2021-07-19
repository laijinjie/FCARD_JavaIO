/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Door.Access.Door8800.Command.System;

import Door.Access.Command.CommandParameter;
import Door.Access.Connector.INConnectorEvent;
import Door.Access.Door8800.Command.Door8800Command;
import Door.Access.Door8800.Command.System.Result.ReadKeepAliveInterval_Result;
import Door.Access.Door8800.Packet.Door8800PacketModel;
import io.netty.buffer.ByteBuf;

/**
 * 读取控制器作为客户端时，和服务器的保活间隔时间.<br>
 * 成功返回结果参考 {@link ReadKeepAliveInterval_Result}
 *
 * @author 赖金杰
 */
public class ReadKeepAliveInterval extends Door8800Command {

    public ReadKeepAliveInterval(CommandParameter par) {
        _Parameter = par;
        CreatePacket(1, 0xF0, 3);
    }

    @Override
    protected void Release0() {
        return;
    }

    @Override
    protected boolean _CommandStep(INConnectorEvent oEvent, Door8800PacketModel model) {
        if (CheckResponse_Cmd(model, 1, 0xF0, 3, 2)) {
            ByteBuf buf = model.GetDatabuff();
            //设定返回值
            ReadKeepAliveInterval_Result ret;
            ret = new ReadKeepAliveInterval_Result();
            _Result = ret;
            ret.IntervalTime = buf.readUnsignedShort();
            RaiseCommandCompleteEvent(oEvent);
            return false;
        } else {
            return false;
        }
    }

}

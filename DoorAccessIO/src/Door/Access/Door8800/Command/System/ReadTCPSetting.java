/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Door.Access.Door8800.Command.System;

import Door.Access.Command.CommandParameter;
import Door.Access.Connector.INConnectorEvent;
import Door.Access.Door8800.Command.Data.TCPDetail;
import Door.Access.Door8800.Command.Door8800Command;
import Door.Access.Door8800.Command.System.Result.*;
import Door.Access.Door8800.Packet.Door8800PacketModel;
import io.netty.buffer.ByteBuf;

/**
 * 读取控制器TCP网络参数
 * <p>
 * 成功返回结果参考 {@link ReadTCPSetting_Result}
 *
 * @author 赖金杰
 */
public class ReadTCPSetting extends Door8800Command {

    public ReadTCPSetting(CommandParameter par) {
        _Parameter = par;

        CreatePacket(1, 6);
    }

    @Override
    protected void Release0() {
        return;
    }

    @Override
    protected boolean _CommandStep(INConnectorEvent oEvent, Door8800PacketModel model) {
        if (CheckResponse_Cmd(model, 1, 6, 0, 0x89)) {
            ByteBuf buf = model.GetDatabuff();

            //设定返回值
            TCPDetail tcp = new TCPDetail();
            tcp.SetBytes(buf);

            _Result = new ReadTCPSetting_Result(tcp);

            RaiseCommandCompleteEvent(oEvent);
            return true;
        } else {
            return false;
        }
    }

}

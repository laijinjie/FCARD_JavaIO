/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Net.PC15.FC8800.Command.System;

import Net.PC15.Command.CommandParameter;
import Net.PC15.Connector.INConnectorEvent;
import Net.PC15.FC8800.Command.Data.BroadcastDetail;
import Net.PC15.FC8800.Command.FC8800Command;
import Net.PC15.FC8800.Command.System.Result.ReadBroadcast_Result;
import Net.PC15.FC8800.Packet.FC8800PacketModel;
import io.netty.buffer.ByteBuf;

/**
 * 语音段开关参数.<br/>
 * 成功返回结果参考 {@link ReadBroadcast_Result}
 *
 * @author 赖金杰
 */
public class ReadBroadcast extends FC8800Command {

    public ReadBroadcast(CommandParameter par) {
        _Parameter = par;
        CreatePacket(1, 0xA, 0x88);
    }

    @Override
    protected void Release0() {
        return;
    }

    @Override
    protected boolean _CommandStep(INConnectorEvent oEvent, FC8800PacketModel model) {
        if (CheckResponse_Cmd(model, 1, 0xA, 0x88, 10)) {
            ByteBuf buf = model.GetDatabuff();
            //设定返回值
            ReadBroadcast_Result ret = new ReadBroadcast_Result();
            _Result = ret;
            byte[] data = new byte[10];
            buf.readBytes(data, 0, 10);
            BroadcastDetail b = new BroadcastDetail(data);
            ret.Broadcast = b;
            RaiseCommandCompleteEvent(oEvent);
            return true;
        } else {
            return false;
        }
    }

}

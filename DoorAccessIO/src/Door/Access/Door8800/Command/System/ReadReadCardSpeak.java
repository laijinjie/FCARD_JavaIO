/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Door.Access.Door8800.Command.System;

import Door.Access.Command.CommandParameter;
import Door.Access.Connector.INConnectorEvent;
import Door.Access.Door8800.Command.Data.ReadCardSpeak;
import Door.Access.Door8800.Command.Door8800Command;
import Door.Access.Door8800.Command.System.Result.ReadReadCardSpeak_Result;
import Door.Access.Door8800.Packet.Door8800PacketModel;
import io.netty.buffer.ByteBuf;

/**
 * 定时读卡播报语音参数.<br/>
 * 成功返回结果参考 {@link ReadReadCardSpeak_Result}
 * @author 赖金杰
 */
public class ReadReadCardSpeak extends Door8800Command {

    public ReadReadCardSpeak(CommandParameter par) {
        _Parameter = par;
        CreatePacket(1, 0xA, 0x91);
    }

    @Override
    protected void Release0() {
        return;
    }

    @Override
    protected boolean _CommandStep(INConnectorEvent oEvent, Door8800PacketModel model) {
        if (CheckResponse_Cmd(model, 1, 0xA, 0x91, 0xA)) {
            ByteBuf buf = model.GetDatabuff();
            //设定返回值
            ReadReadCardSpeak_Result ret = new ReadReadCardSpeak_Result();
            _Result = ret;
            ReadCardSpeak SpeakSetting = new ReadCardSpeak();
            SpeakSetting.SetBytes(buf);
            ret.SpeakSetting = SpeakSetting;
            RaiseCommandCompleteEvent(oEvent);
            return true;
        } else {
            return false;
        }
    }
}

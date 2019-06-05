/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Net.PC15.FC8800.Command.System;

import Net.PC15.Command.CommandParameter;
import Net.PC15.Connector.INConnectorEvent;
import Net.PC15.FC8800.Command.Data.ReadCardSpeak;
import Net.PC15.FC8800.Command.FC8800Command;
import Net.PC15.FC8800.Command.System.Result.ReadReadCardSpeak_Result;
import Net.PC15.FC8800.Packet.FC8800PacketModel;
import io.netty.buffer.ByteBuf;

/**
 * 定时读卡播报语音参数.<br/>
 * 成功返回结果参考 {@link ReadReadCardSpeak_Result}
 * @author 赖金杰
 */
public class ReadReadCardSpeak extends FC8800Command {

    public ReadReadCardSpeak(CommandParameter par) {
        _Parameter = par;
        CreatePacket(1, 0xA, 0x91);
    }

    @Override
    protected void Release0() {
        return;
    }

    @Override
    protected boolean _CommandStep(INConnectorEvent oEvent, FC8800PacketModel model) {
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

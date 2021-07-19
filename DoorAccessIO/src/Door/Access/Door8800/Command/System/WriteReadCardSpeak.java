/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Door.Access.Door8800.Command.System;

import Door.Access.Door8800.Command.Door8800Command;
import Door.Access.Door8800.Command.System.Parameter.WriteReadCardSpeak_Parameter;
import io.netty.buffer.ByteBuf;

/**
 * 定时读卡播报语音参数.<br>
 * 成功返回结果参考 {@link WriteReadCardSpeak_Parameter}
 *
 * @author 赖金杰
 */
public class WriteReadCardSpeak extends Door8800Command {

    public WriteReadCardSpeak(WriteReadCardSpeak_Parameter par) {
        _Parameter = par;

        ByteBuf dataBuf = par.SpeakSetting.GetBytes();
        CreatePacket(1, 0xA, 6, 0xA, dataBuf);
    }

    @Override
    protected void Release0() {
        return;
    }

}

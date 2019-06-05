/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Net.PC15.FC8800.Command.System;

import Net.PC15.FC8800.Command.FC8800Command;
import Net.PC15.FC8800.Command.System.Parameter.CloseAlarm_Parameter;
import Net.PC15.Util.ByteUtil;
import io.netty.buffer.ByteBuf;

/**
 * 解除报警
 *
 * @author 赖金杰
 */
public class CloseAlarm extends FC8800Command {

    public CloseAlarm(CloseAlarm_Parameter par) {
        _Parameter = par;
        ByteBuf dataBuf = ByteUtil.ALLOCATOR.buffer(3);
        dataBuf.writeByte(par.Door);
        dataBuf.writeShort(par.Alarm.Alarm);
        CreatePacket(1, 0xD, 0, 3, dataBuf);
    }

    @Override
    protected void Release0() {
        return;
    }

}

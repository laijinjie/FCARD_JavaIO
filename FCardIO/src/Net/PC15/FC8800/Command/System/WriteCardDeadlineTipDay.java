/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Net.PC15.FC8800.Command.System;

import Net.PC15.FC8800.Command.FC8800Command;
import Net.PC15.FC8800.Command.System.Parameter.WriteCardDeadlineTipDay_Parameter;
import Net.PC15.Util.ByteUtil;
import io.netty.buffer.ByteBuf;

/**
 * 有效期即将过期提醒时间
 *
 * @author 赖金杰
 */
public class WriteCardDeadlineTipDay extends FC8800Command {

    public WriteCardDeadlineTipDay(WriteCardDeadlineTipDay_Parameter par) {
        _Parameter = par;

        ByteBuf dataBuf = ByteUtil.ALLOCATOR.buffer(1);
        dataBuf.writeByte(par.Day);
        CreatePacket(1, 0x15, 0, 1, dataBuf);
    }

    @Override
    protected void Release0() {
        return;
    }

}

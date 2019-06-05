/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Net.PC15.FC8800.Command.System;

import Net.PC15.FC8800.Command.FC8800Command;
import Net.PC15.FC8800.Command.System.Parameter.WriteBuzzer_Parameter;
import Net.PC15.Util.ByteUtil;
import io.netty.buffer.ByteBuf;

/**
 * 主板蜂鸣器参数
 *
 * @author 赖金杰
 */
public class WriteBuzzer extends FC8800Command {

    public WriteBuzzer(WriteBuzzer_Parameter par) {
        _Parameter = par;

        ByteBuf dataBuf = ByteUtil.ALLOCATOR.buffer(1);
        dataBuf.writeByte(par.Buzzer);
        CreatePacket(1, 0xA, 0xA, 1, dataBuf);
    }

    @Override
    protected void Release0() {
        return;
    }

}

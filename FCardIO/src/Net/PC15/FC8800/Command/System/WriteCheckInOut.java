/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Net.PC15.FC8800.Command.System;

import Net.PC15.FC8800.Command.FC8800Command;
import Net.PC15.FC8800.Command.System.Parameter.WriteCheckInOut_Parameter;
import Net.PC15.Util.ByteUtil;
import io.netty.buffer.ByteBuf;

/**
 * 防潜回参数
 *
 * @author 赖金杰
 */
public class WriteCheckInOut extends FC8800Command {

    public WriteCheckInOut(WriteCheckInOut_Parameter par) {
        _Parameter = par;

        ByteBuf dataBuf = ByteUtil.ALLOCATOR.buffer(1);
        dataBuf.writeByte(par.Mode);
        CreatePacket(1, 0xA, 0xF, 1, dataBuf);
    }

    @Override
    protected void Release0() {
        return;
    }

}

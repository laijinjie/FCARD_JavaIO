/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Net.PC15.FC8800.Command.System;

import Net.PC15.FC8800.Command.FC8800Command;
import Net.PC15.FC8800.Command.System.Parameter.WriteDeadline_Parameter;
import Net.PC15.Util.ByteUtil;
import io.netty.buffer.ByteBuf;

/**
 * 写入控制器有效期，剩余天数。
 *
 * @author 赖金杰
 */
public class WriteDeadline extends FC8800Command {

    public WriteDeadline(WriteDeadline_Parameter par) {
        _Parameter = par;
        int Deadline = par.GetDeadline();

        ByteBuf dataBuf = ByteUtil.ALLOCATOR.buffer(2);
        dataBuf.writeShort(Deadline);
        CreatePacket(1, 7, 1, 2, dataBuf);
    }

    @Override
    protected void Release0() {
        return;
    }

}

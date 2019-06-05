/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Net.PC15.FC8800.Command.System;

import Net.PC15.FC8800.Command.FC8800Command;
import Net.PC15.FC8800.Command.System.Parameter.WriteCheck485Line_Parameter;
import Net.PC15.Util.ByteUtil;
import io.netty.buffer.ByteBuf;

/**
 * 485线路反接检测开关
 *
 * @author 赖金杰
 */
public class WriteCheck485Line extends FC8800Command {

    public WriteCheck485Line(WriteCheck485Line_Parameter par) {
        _Parameter = par;

        ByteBuf dataBuf = ByteUtil.ALLOCATOR.buffer(1);
        int v = 0;
        if (par.Use) {
            v = 1;
        }
        dataBuf.writeByte(v);
        CreatePacket(1, 0x13, 0, 1, dataBuf);
    }

    @Override
    protected void Release0() {
        return;
    }

}

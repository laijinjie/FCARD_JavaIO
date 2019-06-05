/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Net.PC15.FC8800.Command.System;

import Net.PC15.FC8800.Command.FC8800Command;
import Net.PC15.FC8800.Command.System.Parameter.WriteExploreLockMode_Parameter;
import Net.PC15.Util.ByteUtil;
import io.netty.buffer.ByteBuf;

/**
 * 防探测功能开关
 *
 * @author 赖金杰
 */
public class WriteExploreLockMode extends FC8800Command {

    public WriteExploreLockMode(WriteExploreLockMode_Parameter par) {
        _Parameter = par;

        ByteBuf dataBuf = ByteUtil.ALLOCATOR.buffer(1);
        int v = 0;
        if (par.Use) {
            v = 1;
        }
        dataBuf.writeByte(v);
        CreatePacket(1, 0x12, 2, 1, dataBuf);
    }

    @Override
    protected void Release0() {
        return;
    }

}

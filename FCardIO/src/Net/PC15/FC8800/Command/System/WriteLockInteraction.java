/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Net.PC15.FC8800.Command.System;

import Net.PC15.FC8800.Command.FC8800Command;
import Net.PC15.FC8800.Command.System.Parameter.WriteLockInteraction_Parameter;
import Net.PC15.Util.ByteUtil;
import io.netty.buffer.ByteBuf;

/**
 * 互锁功能开关
 *
 * @author 赖金杰
 */
public class WriteLockInteraction extends FC8800Command {

    public WriteLockInteraction(WriteLockInteraction_Parameter par) {
        _Parameter = par;

        ByteBuf dataBuf = ByteUtil.ALLOCATOR.buffer(4);
        dataBuf.writeBytes(par.DoorPort.DoorPort);
        CreatePacket(1, 0xA, 4, 4, dataBuf);
    }

    @Override
    protected void Release0() {
        return;
    }

}

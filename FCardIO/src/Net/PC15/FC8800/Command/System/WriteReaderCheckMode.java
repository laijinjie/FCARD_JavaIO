/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Net.PC15.FC8800.Command.System;
import Net.PC15.FC8800.Command.FC8800Command;
import Net.PC15.FC8800.Command.System.Parameter.WriteReaderCheckMode_Parameter;
import Net.PC15.Util.ByteUtil;
import io.netty.buffer.ByteBuf;

/**
 * 读卡器校验参数
 * @author 赖金杰
 */
public class WriteReaderCheckMode extends FC8800Command {

    public WriteReaderCheckMode(WriteReaderCheckMode_Parameter par) {
        _Parameter = par;

        ByteBuf dataBuf = ByteUtil.ALLOCATOR.buffer(1);
        dataBuf.writeByte(par.ReaderCheckMode);
        CreatePacket(1, 0xA, 9, 1, dataBuf);
    }

    @Override
    protected void Release0() {
        return;
    }
    
}

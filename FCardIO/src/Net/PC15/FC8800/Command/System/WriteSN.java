/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Net.PC15.FC8800.Command.System;

import Net.PC15.FC8800.Command.FC8800Command;
import Net.PC15.FC8800.Command.System.Parameter.WriteSN_Parameter;
import Net.PC15.Util.ByteUtil;
import io.netty.buffer.ByteBuf;

/**
 * 写控制器SN
 *
 * @author 赖金杰
 */
public class WriteSN extends FC8800Command {

    private static final byte[] DataStrt;
    private static final byte[] DataEnd;

    static {
        DataStrt = new byte[]{(byte) 0x03, (byte) 0xC5, (byte) 0x89, (byte) 0x12, (byte) 0x3E};
        DataEnd = new byte[]{(byte) 0x90, (byte) 0x7F, (byte) 0x78};
    }

    public WriteSN(WriteSN_Parameter par) {
        _Parameter = par;
        String SN = par.GetSN();

        ByteBuf dataBuf = ByteUtil.ALLOCATOR.buffer(18);
        dataBuf.writeBytes(DataStrt);
        dataBuf.writeBytes(SN.getBytes());
        dataBuf.writeBytes(DataEnd);
        CreatePacket(1, 1, 0, 18, dataBuf);
    }

    @Override
    protected void Release0() {
        return;
    }

}

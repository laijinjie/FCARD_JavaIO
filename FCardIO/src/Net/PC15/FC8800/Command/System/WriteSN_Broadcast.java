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
 * 通过广播写SN
 *
 * @author 赖金杰
 */
public class WriteSN_Broadcast extends FC8800Command {

    private static final byte[] DataStrt;
    private static final byte[] DataEnd;

    static {
        DataStrt = new byte[]{(byte) 0xFC, (byte) 0x65, (byte) 0x65, (byte) 0x33, (byte) 0xFF};
        DataEnd = new byte[]{(byte) 0xCF, (byte) 0x35, (byte) 0x92};
    }

    public WriteSN_Broadcast(WriteSN_Parameter par) {
        _Parameter = par;
        String SN = par.GetSN();

        ByteBuf dataBuf = ByteUtil.ALLOCATOR.buffer(18);
        dataBuf.writeBytes(DataStrt);
        dataBuf.writeBytes(SN.getBytes());
        dataBuf.writeBytes(DataEnd);
        CreatePacket(0xC1, 0xD1, 0xF7, 18, dataBuf);
    }

    @Override
    protected void Release0() {
        return;
    }

}

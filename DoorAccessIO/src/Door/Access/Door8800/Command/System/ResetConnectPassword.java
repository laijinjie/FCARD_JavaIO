/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Door.Access.Door8800.Command.System;

import Door.Access.Command.CommandParameter;
import Door.Access.Door8800.Command.Door8800Command;
import Door.Access.Util.ByteUtil;
import io.netty.buffer.ByteBuf;

/**
 * 重置控制器通讯密码，重置后控制器通讯密码恢复为0xFFFFFFFF
 *
 * @author 赖金杰
 */
public class ResetConnectPassword extends Door8800Command {

    private static final byte[] DataStrt;

    static {
        //46 43 61 72 64 59 7A
        DataStrt = new byte[]{(byte) 0x46, (byte) 0x43, (byte) 0x61, (byte) 0x72, (byte) 0x64, (byte) 0x59, (byte) 0x7A};
    }

    public ResetConnectPassword(CommandParameter par) {
        _Parameter = par;
        ByteBuf dataBuf = ByteUtil.ALLOCATOR.buffer(7);
        dataBuf.writeBytes(DataStrt);
        CreatePacket(1, 5, 0, 7, dataBuf);
    }

    @Override
    protected void Release0() {
        return;
    }

}

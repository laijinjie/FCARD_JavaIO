/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Door.Access.Door8800.Command.System;

import Door.Access.Door8800.Command.System.Parameter.WriteConnectPassword_Parameter;
import Door.Access.Door8800.Command.Door8800Command;
import Door.Access.Util.ByteUtil;
import Door.Access.Util.StringUtil;
import io.netty.buffer.ByteBuf;

/**
 * 修改控制器通讯密码
 *
 * @author 赖金杰
 */
public class WriteConnectPassword extends Door8800Command {

    private static final byte[] NULLPassword;

    static {
        NULLPassword = new byte[]{(byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF};
    }

    public WriteConnectPassword(WriteConnectPassword_Parameter par) {
        _Parameter = par;
        ByteBuf dataBuf = ByteUtil.ALLOCATOR.buffer(4);
        String pwd = par.GetPassword();
        if (StringUtil.IsNullOrEmpty(pwd)) {

            dataBuf.writeBytes(NULLPassword);
        } else {
            dataBuf.writeBytes(StringUtil.HexToByte(pwd));
        }

        CreatePacket(1, 3, 0, 4, dataBuf);
    }

    @Override
    protected void Release0() {
        return;
    }
}

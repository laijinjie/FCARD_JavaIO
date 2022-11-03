/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Door.Access.Door89H.Command.Password;

import Door.Access.Door8800.Command.Door8800Command;

import Door.Access.Door89H.Command.Data.PasswordDetail;
import Door.Access.Door89H.Command.Password.Parameter.DeletePassword_Parameter;
import Door.Access.Util.ByteUtil;
import Door.Access.Util.StringUtil;
import io.netty.buffer.ByteBuf;
import java.util.ArrayList;

/**
 *
 * @author F
 */
public class DeletePassword extends Door8800Command {

    protected String[] PasswordsStrings;

    public DeletePassword(DeletePassword_Parameter par) {
        _Parameter=par;
        PasswordsStrings = par.PasswordsStrings;
        _CreatePacket();
    }

    public void _CreatePacket() {
        int size = PasswordsStrings.length;
        int bufSize = 4 + (size * 4);
        ByteBuf buf = ByteUtil.ALLOCATOR.buffer(bufSize);
        buf.writeInt(size);
        for (String password : PasswordsStrings) {
             password = StringUtil.FillHexString(password, 8, "F", true);
            long pwd = Long.parseLong(password, 16);
            buf.writeInt((int)pwd);
        }
        CreatePacket(0x05, 0x05, 0x00, bufSize, buf);
    }

    @Override
    protected void Release0() {

    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Door.Access.Door8800.Command.Password;

import Door.Access.Door8800.Command.Data.PasswordDetail;
import Door.Access.Door8800.Command.Door8800Command;
import Door.Access.Door8800.Command.Password.Parameter.DeletePassword_Parameter;
import Door.Access.Util.ByteUtil;
import io.netty.buffer.ByteBuf;
import java.util.ArrayList;

/**
 * 删除开门密码
 *
 * @author F
 */
public class DeletePassword extends Door8800Command {

   protected ArrayList<PasswordDetail> passwordDetails;

    public DeletePassword(DeletePassword_Parameter par) {
        _Parameter=par;
        passwordDetails = par.passwordDetails;
         _CreatePacket();
    }
    public void _CreatePacket(){
        int size=passwordDetails.size();
        int bufSize=0x04*(size*5);
        ByteBuf buf=ByteUtil.ALLOCATOR.buffer(bufSize);
        buf.writeInt(size);
        for (PasswordDetail passwordDetail : passwordDetails) {
            passwordDetail.GetBytes(buf);
        }
        CreatePacket(0x05, 0x05, 0x00, bufSize, buf);
    }
    
    @Override
    protected void Release0() {

    }

}

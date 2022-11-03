/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Door.Access.Door89H.Command.Password;

import Door.Access.Door89H.Command.Data.PasswordDetail;
import Door.Access.Door8800.Command.Password.Parameter.WritePassword_Parameter;
import Door.Access.Util.ByteUtil;
import io.netty.buffer.ByteBuf;

import java.util.ArrayList;

/**
 * 写入开门密码
 *
 * @author F
 */
public class WritePassword extends Door.Access.Door8800.Command.Password.WritePassword {


    public WritePassword(WritePassword_Parameter par) {
        super(par);
        Page = 15;
    }

    @Override
    protected ByteBuf GetBytes() {
        int size = 4 + (12 * Page);
        ByteBuf dataBuf = ByteUtil.ALLOCATOR.buffer(size);
        dataBuf.writeInt(Page);
        int maxCount = Index + Page;
        if(maxCount>UploadMax){
            maxCount=UploadMax;
        }
        for (int i = Index; i < UploadMax; i++) {
            PasswordDetail passwordDetail = (PasswordDetail) par.passwordDetails.get(i);
            passwordDetail.GetBytes(dataBuf);
            if (maxCount == (i+1) ) {
                break;
            }
        }
        return dataBuf;
    }
}

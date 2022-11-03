/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Door.Access.Door89H.Command.Password;

import Door.Access.Command.CommandParameter;
import Door.Access.Door89H.Command.Data.PasswordDetail;
import Door.Access.Door8800.Command.Password.Result.ReadPasswordDataBase_Result;
import io.netty.buffer.ByteBuf;
import java.util.ArrayList;

/**
 * 读取所有开门密码89H
 *
 * @author F
 */
public class ReadPasswordDataBase extends Door.Access.Door8800.Command.Password.ReadPasswordDataBase {

//   ArrayList<PasswordDetail> list;

    public ReadPasswordDataBase(CommandParameter par) {
        super(par);
    }

    @Override
    protected void Analysis(ByteBuf buf) {
        int count = buf.readInt();
        for (int i = 0; i < count; i++) {
            PasswordDetail detail = new PasswordDetail();
            detail.SetBytes(buf);
            list.add(detail);
        }
        buf.release();
    }

    @Override
    protected void Result() {
        ReadPasswordDataBase_Result result = new ReadPasswordDataBase_Result();
        result.PasswordDetails = list;
        result.DataBaseSize = PasswordCount;
        _Result = result;
    }

}

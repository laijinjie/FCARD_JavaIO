package Face.System;

import Door.Access.Door8800.Command.Door8800Command;
import Door.Access.Util.ByteUtil;
import Door.Access.Util.StringUtil;
import Face.System.Parameter.WriteManageMenuPassword_Parameter;
import io.netty.buffer.ByteBuf;

/**
 * 写入管理密码
 */
public class WriteManageMenuPassword extends Door8800Command {
    public  WriteManageMenuPassword(WriteManageMenuPassword_Parameter parameter){
        _Parameter=parameter;
        ByteBuf buf = ByteUtil.ALLOCATOR.buffer(4);
        StringUtil.HextoByteBuf(parameter.Password,buf);
        CreatePacket(0x01, 0x1d, 0x00,4,buf);
    }
    @Override
    protected void Release0() {

    }
}

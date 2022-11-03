package Face.Door;

import Door.Access.Door8800.Command.Door8800Command;
import Door.Access.Util.ByteUtil;
import Face.Door.Parameter.ExpirationPrompt_Parameter;
import io.netty.buffer.ByteBuf;

/**
 * 权限到期提示参数——写入
 * @author F
 */
public class WriteExpirationPrompt extends Door8800Command {
    /**
     * 权限到期提示参数
     * @param parameter 权限到期提示参数 参数
     */
    public WriteExpirationPrompt(ExpirationPrompt_Parameter parameter){
        _Parameter=parameter;
        CreatePacket(0x03,0x15,0x00,2,getDataBuf(parameter));
    }
    private ByteBuf getDataBuf(ExpirationPrompt_Parameter parameter){
        ByteBuf buf= ByteUtil.ALLOCATOR.buffer(2);
        buf.writeBoolean(parameter.IsUse);
        buf.writeByte(parameter.Time);
        return  buf;
    }
    @Override
    protected void Release0() {

    }
}

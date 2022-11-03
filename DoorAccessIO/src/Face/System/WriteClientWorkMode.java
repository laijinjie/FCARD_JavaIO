package Face.System;

import Door.Access.Door8800.Command.Door8800Command;
import Door.Access.Util.ByteUtil;
import Face.System.Parameter.WriteClientWorkMode_Parameter;
import io.netty.buffer.ByteBuf;

/**
 * 写入客户端模式通讯方式<br>
 */
public class WriteClientWorkMode extends Door8800Command {
    /**
     * 写入客户端模式通讯方式
     * @param parameter
     */
    public WriteClientWorkMode(WriteClientWorkMode_Parameter parameter) {
        _Parameter = parameter;
        ByteBuf buf = ByteUtil.ALLOCATOR.buffer(1);
        parameter.GetBytes(buf);
        CreatePacket(0x01, 0x30, 0x02, 1, buf);
    }

    @Override
    protected void Release0() {

    }
}

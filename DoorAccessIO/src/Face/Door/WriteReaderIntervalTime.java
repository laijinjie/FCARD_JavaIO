package Face.Door;

import Door.Access.Door8800.Command.Door8800Command;
import Door.Access.Util.ByteUtil;
import Face.Door.Parameter.ReaderIntervalTime_Parameter;
import io.netty.buffer.ByteBuf;

/**
 * 设置重复验证权限间隔——写入
 *
 * @author F
 */
public class WriteReaderIntervalTime extends Door8800Command {
    int DATA_LEN=4;
    /**
     * 设置重复验证权限间隔
     * @param parameter 设置重复验证权限间隔参数
     */
    public WriteReaderIntervalTime(ReaderIntervalTime_Parameter parameter) {
        _Parameter = parameter;
        CreatePacket(0x03, 0x14, 0x00, DATA_LEN, getDataBuf(parameter));
    }

    private ByteBuf getDataBuf(ReaderIntervalTime_Parameter parameter) {
        ByteBuf buf = ByteUtil.ALLOCATOR.buffer(DATA_LEN);
        buf.writeBoolean(parameter.IsUse);
        buf.writeByte(parameter.Mode);
        buf.writeShort(parameter.IntervalTime);
        return buf;
    }

    @Override
    protected void Release0() {

    }
}

package Face.Elevator.System.TimingOpen;

import Door.Access.Door8800.Command.Door8800Command;
import io.netty.buffer.ByteBuf;

/**
 * 设置定时常开参数的命令
 */
public class WriteTimingOpen extends Door8800Command {
    /**
     * 创建设置定时常开参数的命令
     * @param parameter 设置定时常开参数的命令 参数
     */
    public WriteTimingOpen(WriteTimingOpen_Parameter parameter) {
        _Parameter = parameter;
        parameter.checkedParameter();
        ByteBuf buf = parameter.GetBytes();
        CreatePacket(0x03, 0x26, 0x01, buf.readableBytes(), buf);
    }
    @Override
    protected void Release0() {

    }
}

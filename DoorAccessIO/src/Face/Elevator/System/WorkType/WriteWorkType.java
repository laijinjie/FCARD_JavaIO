package Face.Elevator.System.WorkType;

import Door.Access.Door8800.Command.Door8800Command;
import io.netty.buffer.ByteBuf;

/**
 * 设置电梯工作模式
 */
public class WriteWorkType extends Door8800Command {
    /**
     * 创建设置电梯工作模式
     * @param parameter 设置电梯工作模式参数
     */
    public WriteWorkType(WriteWorkType_Parameter parameter) {
        _Parameter = parameter;
        ByteBuf buf = parameter.GetBytes();
        CreatePacket(0x03, 0x21, 0x01, buf.readableBytes(), buf);
    }

    @Override
    protected void Release0() {

    }
}

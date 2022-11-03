package Face.Elevator.System.WorkType;

import Door.Access.Command.INCommandResult;
import io.netty.buffer.ByteBuf;

/**
 * 读取电梯工作模式 返回结果
 */
public class ReadWorkType_Result implements INCommandResult {
    /**
     * 电梯工作模式
     * 0--禁用电梯模式
     * 1--启动电梯模式
     */
    public byte WorkType;

    @Override
    public void release() {

    }

    public void SetBytes(ByteBuf buf) {
        WorkType = buf.readByte();
    }
}

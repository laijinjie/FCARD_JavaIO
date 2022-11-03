package Face.Elevator.Person;

import Door.Access.Command.INCommandResult;
import io.netty.buffer.ByteBuf;

import java.util.ArrayList;

public class ReadPersonElevatorAccess_Result implements INCommandResult {

    /***
     * 用户号
     */
    public long UserCode;
    /**
     * 状态 1--表示成功；0--表示用户号未登记；2--表示不支持此功能
     */
    public byte Status;
    /**
     * 继电器权限列表，固定64个元素，每个元素代表一个继电器权限 权限说明：0表示无权限，1表示有权限
     */
    public ArrayList<Byte> RelayAccesss;

    @Override
    public void release() {

    }

    public void SetBytes(ByteBuf buf) {
        if (buf.readableBytes() < 68) {
            throw new RuntimeException("ByteBuf error ");
        }
        if (RelayAccesss == null) {
            RelayAccesss = new ArrayList<>();
        }
        UserCode = buf.readUnsignedInt();
        Status = buf.readByte();
        for (int i = 0; i < 64; i++) {
            RelayAccesss.add(buf.readByte());
        }
    }
}

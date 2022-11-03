package Face.Elevator.System.ReleaseTime;

import Door.Access.Door8800.Command.Door8800Command;
import io.netty.buffer.ByteBuf;

public class WriteReleaseTime extends Door8800Command {
    public WriteReleaseTime(WriteReleaseTime_Parameter parameter) {
        _Parameter = parameter;
        ByteBuf buf = parameter.GetBytes();
        CreatePacket(0x03, 0x28, 0x01, buf.readableBytes(), buf);
    }

    @Override
    protected void Release0() {

    }
}

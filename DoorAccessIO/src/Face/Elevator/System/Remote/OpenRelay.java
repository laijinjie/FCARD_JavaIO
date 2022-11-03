package Face.Elevator.System.Remote;

import Door.Access.Door8800.Command.Door8800Command;
import io.netty.buffer.ByteBuf;

public class OpenRelay extends Door8800Command {

    public OpenRelay(RemoteRelay_Parameter parameter) {
        _Parameter = parameter;
        ByteBuf buf = parameter.GetBytes();
        CreatePacket(3, RemoteCommandCode(), RemoteCommandPar(), buf.readableBytes(), buf);
    }

    protected int RemoteCommandCode() {
        return 0x23;
    }

    protected int RemoteCommandPar() {
        return 0;
    }

    @Override
    protected void Release0() {

    }
}

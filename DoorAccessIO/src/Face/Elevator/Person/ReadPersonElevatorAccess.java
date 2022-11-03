package Face.Elevator.Person;

import Door.Access.Connector.INConnectorEvent;
import Door.Access.Door8800.Command.Door8800Command;
import Door.Access.Door8800.Packet.Door8800PacketModel;
import io.netty.buffer.ByteBuf;

public class ReadPersonElevatorAccess extends Door8800Command {

    public ReadPersonElevatorAccess(ReadPersonElevatorAccess_Parameter parameter) {
        _Parameter = parameter;
        ByteBuf buf = parameter.GetBytes();
        CreatePacket(7, 6, 1, buf.readableBytes(), buf);
    }

    @Override
    protected boolean _CommandStep(INConnectorEvent oEvent, Door8800PacketModel model) {
        if (CheckResponse_Cmd(model, 69)) {
            ReadPersonElevatorAccess_Result result = new ReadPersonElevatorAccess_Result();
            try {
                result.SetBytes(model.GetDatabuff());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            _Result = result;
            RaiseCommandCompleteEvent(oEvent);
        }
        return true;
    }

    @Override
    protected void Release0() {

    }
}

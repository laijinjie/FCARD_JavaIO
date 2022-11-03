package Face.Elevator.Person;

import Door.Access.Connector.INConnectorEvent;
import Door.Access.Door8800.Command.Door8800Command;
import Door.Access.Door8800.Packet.Door8800PacketModel;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;

public class WritePersonElevatorAccess extends Door8800Command {

    public WritePersonElevatorAccess(WritePersonElevatorAccess_Parameter parameter) {
        _Parameter = parameter;
        parameter.CheckParameter();
        ByteBuf buf = parameter.GetBytes();
   //  System.out.println( ByteBufUtil.hexDump(buf));
        CreatePacket(7, 6, 0, buf.readableBytes(), buf);
    }

    @Override
    protected boolean _CommandStep(INConnectorEvent oEvent, Door8800PacketModel model) {
        if (CheckResponse_Cmd(model, 5)) {
            WritePersonElevatorAccess_Result result = new WritePersonElevatorAccess_Result();
            _Result = result;
            result.SetBytes(model.GetDatabuff());
            RaiseCommandCompleteEvent(oEvent);
        }

        return true;
    }

    @Override
    protected void Release0() {

    }
}

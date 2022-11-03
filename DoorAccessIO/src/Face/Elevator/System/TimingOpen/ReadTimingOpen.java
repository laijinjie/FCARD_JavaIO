package Face.Elevator.System.TimingOpen;

import Door.Access.Connector.INConnectorEvent;
import Door.Access.Door8800.Command.Door8800Command;
import Door.Access.Door8800.Packet.Door8800PacketModel;
import Door.Access.Util.ByteUtil;
import io.netty.buffer.ByteBuf;

/**
 * 读取定时常开参数
 */
public class ReadTimingOpen extends Door8800Command {

    public ReadTimingOpen(ReadTimingOpen_Parameter parameter) {
        _Parameter = parameter;
        ByteBuf buf = ByteUtil.ALLOCATOR.buffer(1);
        buf.writeByte(parameter.Port);
        CreatePacket(0x03, 0x26, 0x00, 0x01, buf);
    }

    @Override
    protected boolean _CommandStep(INConnectorEvent oEvent, Door8800PacketModel model) {
        if (CheckResponse_Cmd(model, 0xE3)) {
            ReadTimingOpen_Result result = new ReadTimingOpen_Result();
            result.SetBytes(model.GetDatabuff());
            _Result = result;
            RaiseCommandCompleteEvent(oEvent);
        }
        return true;
    }

    @Override
    protected void Release0() {

    }
}

package Face.Elevator.System.ReleaseTime;

import Door.Access.Command.CommandParameter;
import Door.Access.Connector.INConnectorEvent;
import Door.Access.Door8800.Command.Door8800Command;
import Door.Access.Door8800.Packet.Door8800PacketModel;

/**
 * 读取电梯继电器板的继电器开锁输出时长
 */
public class ReadReleaseTime extends Door8800Command {

    public ReadReleaseTime(CommandParameter parameter) {
        _Parameter = parameter;
        CreatePacket(0x03, 0x28);
    }

    @Override
    protected boolean _CommandStep(INConnectorEvent oEvent, Door8800PacketModel model) {
        if (CheckResponse_Cmd(model, 128)) {
            ReadReleaseTime_Result result = new ReadReleaseTime_Result();
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

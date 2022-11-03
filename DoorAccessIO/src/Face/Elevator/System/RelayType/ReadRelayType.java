package Face.Elevator.System.RelayType;

import Door.Access.Command.CommandParameter;
import Door.Access.Connector.INConnectorEvent;
import Door.Access.Door8800.Command.Door8800Command;
import Door.Access.Door8800.Packet.Door8800PacketModel;

/**
 * 读取电梯继电器板的继电器输出类型的命令
 */
public class ReadRelayType extends Door8800Command {
    /**
     * 读取电梯继电器板的继电器输出类型的命令
     *
     * @param parameter 命令详情
     */
    public ReadRelayType(CommandParameter parameter) {
        _Parameter = parameter;
        CreatePacket(0x03, 0x22);
    }

    @Override
    protected boolean _CommandStep(INConnectorEvent oEvent, Door8800PacketModel model) {
        if (CheckResponse_Cmd(model, 64)) {
            ReadRelayType_Result result = new ReadRelayType_Result();
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

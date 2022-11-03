package Face.System;

import Door.Access.Command.CommandParameter;
import Door.Access.Connector.INConnectorEvent;
import Door.Access.Door8800.Command.Door8800Command;
import Door.Access.Door8800.Packet.Door8800PacketModel;
import Face.System.Result.ReadClientWorkMode_Result;

/**
 * 读取客户端模式通讯方式<br>
 */
public class ReadClientWorkMode extends Door8800Command {
    /**
     * 读取客户端模式通讯方式
     * @param parameter
     */
    public ReadClientWorkMode(CommandParameter parameter) {
        _Parameter = parameter;
        CreatePacket(0x01, 0x30, 0x03);
    }

    @Override
    protected boolean _CommandStep(INConnectorEvent oEvent, Door8800PacketModel model) {
        boolean IsCommandComplete=false;
        if (CheckResponse_Cmd(model, 1)) {
            ReadClientWorkMode_Result result = new ReadClientWorkMode_Result();
            result.SetBytes(model.GetDatabuff());
            _Result = result;
            IsCommandComplete = true;
        }
        RaiseCommandCompleteEvent(oEvent);
        return IsCommandComplete;
    }
    @Override
    protected void Release0() {

    }
}

package Face.System;

import Door.Access.Command.CommandParameter;
import Door.Access.Connector.INConnectorEvent;
import Door.Access.Door8800.Command.Door8800Command;
import Door.Access.Door8800.Packet.Door8800PacketModel;
import Face.System.Result.ReadWatchState_Result;

/**
 * 读取监控状态
 */
public class ReadWatchState extends Door8800Command {
    
    /**
     * 读取监控状态
     */
    public ReadWatchState(CommandParameter parameter) {
        _Parameter = parameter;
        CreatePacket(0x01, 0x0B, 0x02);
    }

    @Override
    protected boolean _CommandStep(INConnectorEvent oEvent, Door8800PacketModel model) {

        if (CheckResponse_Cmd(model, 0x01)) {
            ReadWatchState_Result result = new ReadWatchState_Result();
            result.WatchState = model.GetDatabuff().readByte();
            _Result = result;
            RaiseCommandCompleteEvent(oEvent);
            return true;
        }
        return false;
    }

    @Override
    protected void Release0() {

    }
}

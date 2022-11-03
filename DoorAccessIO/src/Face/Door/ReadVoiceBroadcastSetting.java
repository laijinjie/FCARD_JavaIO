package Face.Door;

import Door.Access.Command.CommandParameter;
import Door.Access.Connector.INConnectorEvent;
import Door.Access.Door8800.Command.Door8800Command;
import Door.Access.Door8800.Packet.Door8800PacketModel;
import Face.Door.Result.VoiceBroadcastSetting_Result;
import io.netty.buffer.ByteBuf;

/**
 * 设置语音播报功能_读取
 *
 * @author F
 */
public class ReadVoiceBroadcastSetting extends Door8800Command {

    int CMD_TYPE = 0x03;
    int CMD_INDEX = 0x13;
    int CMD_PAR = 0x01;

    /**
     * 设置语音播报功能_读取
     *
     * @param parameter 命令参数
     */
    public ReadVoiceBroadcastSetting(CommandParameter parameter) {
        _Parameter = parameter;
        CreatePacket(CMD_TYPE, CMD_INDEX, CMD_PAR);
    }

    @Override
    protected boolean _CommandStep(INConnectorEvent oEvent, Door8800PacketModel model) {
        if (CheckResponse_Cmd(model, CMD_TYPE, CMD_INDEX, CMD_PAR)) {
            ByteBuf buf = model.GetDatabuff();
            VoiceBroadcastSetting_Result result = new VoiceBroadcastSetting_Result();
            result.Use = buf.readBoolean();
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

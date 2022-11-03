package Face.Door;

import Door.Access.Command.CommandParameter;
import Door.Access.Connector.INConnectorEvent;
import Door.Access.Door8800.Command.Door8800Command;
import Door.Access.Door8800.Packet.Door8800PacketModel;
import Face.Door.Result.ExpirationPrompt_Result;
import io.netty.buffer.ByteBuf;

/**
 * 权限到期提示参数——读取
 *
 * @author F
 */
public class ReadExpirationPrompt extends Door8800Command {
    int CMD_TYPE = 0x03;
    int CMD_INDEX = 0x15;
    int CMD_PAR = 0x01;
/**
 * 权限到期提示参数
 * @param parameter 命令参数
 */
    public ReadExpirationPrompt(CommandParameter parameter) {
        _Parameter = parameter;
        CreatePacket(CMD_TYPE, CMD_INDEX, CMD_PAR);
    }

    @Override
    protected boolean _CommandStep(INConnectorEvent oEvent, Door8800PacketModel model) {
        if (CheckResponse_Cmd(model, CMD_TYPE, CMD_INDEX, CMD_PAR)) {
            ByteBuf buf = model.GetDatabuff();
            ExpirationPrompt_Result result = new ExpirationPrompt_Result();
            result.IsUse = buf.readBoolean();
            result.Time = buf.readByte();
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

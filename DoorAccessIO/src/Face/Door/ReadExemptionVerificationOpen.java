package Face.Door;

import Door.Access.Command.CommandParameter;
import Door.Access.Connector.INConnectorEvent;
import Door.Access.Door8800.Command.Door8800Command;
import Door.Access.Door8800.Packet.Door8800PacketModel;
import Face.Door.Result.ExemptionVerificationOpen_Result;
import io.netty.buffer.ByteBuf;

/**
 * 免验证开门_读取
 */
public class ReadExemptionVerificationOpen extends Door8800Command {

    int CMD_TYPE = 0x03;
    int CMD_INDEX = 0x011;
    int CMD_PAR = 0x01;

    /**
     * 免验证开门
     *
     * @param par 命令参数
     */
    public ReadExemptionVerificationOpen(CommandParameter par) {
        _Parameter = par;
        CreatePacket(CMD_TYPE, CMD_INDEX, CMD_PAR);
    }

    @Override
    protected boolean _CommandStep(INConnectorEvent oEvent, Door8800PacketModel model) {
        if (CheckResponse_Cmd(model, CMD_TYPE, CMD_INDEX, CMD_PAR)) {
            ByteBuf buf = model.GetDatabuff();
            ExemptionVerificationOpen_Result result = new ExemptionVerificationOpen_Result();
            result.IsUseExemptionVerification = buf.readBoolean();
            result.IsUseAutomaticRegistration = buf.readBoolean();
            result.PeriodNumber = buf.readByte();
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

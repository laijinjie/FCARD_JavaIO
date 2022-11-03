package Face.Door;

import Door.Access.Command.CommandParameter;
import Door.Access.Connector.INConnectorEvent;
import Door.Access.Door8800.Command.Door8800Command;
import Door.Access.Door8800.Packet.Door8800PacketModel;
import Face.Door.Result.UnlockingTime_Result;
import io.netty.buffer.ByteBuf;

/**
 * 开锁时输出时长
 */
public class ReadUnlockingTime extends Door8800Command {

    /**
     * 分类
     */
    int CMD_TYPE = 0x03;
    /**
     * 命令
     */
    int CMD_INDEX = 0x08;

    /**
     * 开锁时输出时长
     *
     * @param par 命令参数
     */
    public ReadUnlockingTime(CommandParameter par) {
        _Parameter = par;
        CreatePacket(CMD_TYPE, CMD_INDEX);
    }

    @Override
    protected boolean _CommandStep(INConnectorEvent oEvent, Door8800PacketModel model) {
        if (CheckResponse_Cmd(model, CMD_TYPE, CMD_INDEX)) {
            ByteBuf buf = model.GetDatabuff();
            UnlockingTime_Result result = new UnlockingTime_Result();
            result.ReleaseTime = buf.readUnsignedShort();
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

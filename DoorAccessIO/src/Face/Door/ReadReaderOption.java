package Face.Door;

import Door.Access.Command.CommandParameter;
import Door.Access.Connector.INConnectorEvent;
import Door.Access.Door8800.Command.Door8800Command;
import Door.Access.Door8800.Packet.Door8800PacketModel;
import Face.Door.Result.ReaderOption_Result;
import io.netty.buffer.ByteBuf;

/**
 * 读卡器字节数_读取
 *
 * @author F
 */
public class ReadReaderOption extends Door8800Command {
    /**
     * 分类
     */
    int CMD_TYPE = 0x03;
    /**
     * 命令
     */
    int CMD_INDEX = 0X01;
    /**
     * 读卡器字节数
     * @param par 命令参数
     */
    public ReadReaderOption(CommandParameter par) {
        _Parameter = par;
        CreatePacket(CMD_TYPE, CMD_INDEX);
    }

    @Override
    protected void Release0() {
        return;
    }

    @Override
    protected boolean _CommandStep(INConnectorEvent oEvent, Door8800PacketModel model) {
        if (CheckResponse_Cmd(model, CMD_TYPE, CMD_INDEX)) {
            ByteBuf buf = model.GetDatabuff();
            ReaderOption_Result result = new ReaderOption_Result();
            result.ReaderOption = buf.readByte();
            _Result = result;
            RaiseCommandCompleteEvent(oEvent);
            return true;
        }
        return false;
    }
}

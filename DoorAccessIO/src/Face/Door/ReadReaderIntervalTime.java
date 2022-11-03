package Face.Door;

import Door.Access.Command.CommandParameter;
import Door.Access.Connector.INConnectorEvent;
import Door.Access.Door8800.Command.Door8800Command;
import Door.Access.Door8800.Packet.Door8800PacketModel;
import Face.Door.Result.ReaderIntervalTime_Result;
import io.netty.buffer.ByteBuf;

/**
 *重复验证权限间隔——读取
 * @author F
 */
public class ReadReaderIntervalTime extends Door8800Command {

    int CMD_TYPE=0x03;
    int CMD_INDEX=0x14;
    int CMD_PAR=0x01;
    /**
     * 重复验证权限间隔
     * @param parameter  命令参数
     */
    public ReadReaderIntervalTime(CommandParameter parameter){
        _Parameter=parameter;
        CreatePacket(CMD_TYPE,CMD_INDEX,CMD_PAR);
    }

    @Override
    protected boolean _CommandStep(INConnectorEvent oEvent, Door8800PacketModel model) {
        if (CheckResponse_Cmd(model,CMD_TYPE,CMD_INDEX,CMD_PAR)){
            ByteBuf buf=model.GetDatabuff();
            ReaderIntervalTime_Result result=new ReaderIntervalTime_Result();
            result.IsUse=buf.readBoolean();
            result.Mode=buf.readByte();
            result.IntervalTime=buf.readUnsignedShort();
            _Result=result;
            RaiseCommandCompleteEvent(oEvent);
            return true;
        }
        return false;
    }

    @Override
    protected void Release0() {

    }
}

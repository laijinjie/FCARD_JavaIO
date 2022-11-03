package Face.Door;

import Door.Access.Command.CommandParameter;
import Door.Access.Connector.INConnectorEvent;
import Door.Access.Door8800.Command.Door8800Command;
import Door.Access.Door8800.Packet.Door8800PacketModel;
import Face.Door.Result.RelayOption_Result;
import io.netty.buffer.ByteBuf;

/**
 * 继电器参数_读取
 *
 * @author F
 */
public class ReadRelayOption extends Door8800Command {
    /**
     * 分类
     */
    int CMD_TYPE = 0x03;
    /**
     * 命令
     */
    int CMD_INDEX = 0X02;
    /**
     * 继电器参数
     * @param par  命令参数
     */
    public ReadRelayOption(CommandParameter par) {
        _Parameter = par;
        CreatePacket(CMD_TYPE, CMD_INDEX);
    }

    @Override
    protected boolean _CommandStep(INConnectorEvent oEvent, Door8800PacketModel model) {
       if(CheckResponse_Cmd(model,CMD_TYPE,CMD_INDEX)){
           ByteBuf buf=model.GetDatabuff();
           RelayOption_Result result=new RelayOption_Result();
           result.IsSupport=buf.readBoolean();
           _Result=result;
           RaiseCommandCompleteEvent(oEvent);
           return  true;
       }
       return  false;
    }

    @Override
    protected void Release0() {

    }
}

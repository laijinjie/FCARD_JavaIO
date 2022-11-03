package Face.System;

import Door.Access.Command.CommandParameter;
import Door.Access.Connector.INConnectorEvent;
import Door.Access.Door8800.Command.Door8800Command;
import Door.Access.Door8800.Packet.Door8800PacketModel;
import Face.System.Result.ReadWiegandOutput_Result;

/**
 * 读取韦根参数
 */
public class ReadWiegandOutput extends Door8800Command {
    /**
     * 读取韦根参数
     * @param parameter 命令详情
     */
    public ReadWiegandOutput(CommandParameter parameter) {
        _Parameter = parameter;
        CreatePacket(0x01, 0x1c, 0x03);
    }

    @Override
    protected boolean _CommandStep(INConnectorEvent oEvent, Door8800PacketModel model) {
        boolean isCommandComplete=false;
        if (CheckResponse_Cmd(model, 0x04)) {
            ReadWiegandOutput_Result result=new ReadWiegandOutput_Result();
            result.SetBytes(model.GetDatabuff());
            _Result=result;
            RaiseCommandCompleteEvent(oEvent);
            isCommandComplete=true;
        }
        return  isCommandComplete;
    }

    @Override
    protected void Release0() {

    }
}

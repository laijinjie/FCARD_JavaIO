package Face.System;

import Door.Access.Command.CommandParameter;
import Door.Access.Connector.INConnectorEvent;
import Door.Access.Door8800.Command.Door8800Command;
import Door.Access.Door8800.Packet.Door8800PacketModel;
import Face.System.Result.ReadSystemStatus_Result;

/**
 * 读取设备状态
 */
public class ReadSystemStatus extends Door8800Command {
    /**
     * 初始化
     * @param parameter 通讯参数
     */
    public ReadSystemStatus(CommandParameter parameter){
        _Parameter=parameter;
        CreatePacket(0x01, 0x0E);
    }

    @Override
    protected boolean _CommandStep(INConnectorEvent oEvent, Door8800PacketModel model) {
        if(CheckResponse_Cmd(model,0x06)){
            ReadSystemStatus_Result result=new ReadSystemStatus_Result();
            result.SetBytes(model.GetDatabuff());
            _Result=result;
            RaiseCommandCompleteEvent(oEvent);
            return  true;
        }
        return false;
    }

    @Override
    protected void Release0() {

    }
}

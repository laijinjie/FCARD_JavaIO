package Face.System;

import Door.Access.Command.CommandParameter;
import Door.Access.Connector.INConnectorEvent;
import Door.Access.Door8800.Command.Door8800Command;
import Door.Access.Door8800.Packet.Door8800PacketModel;
import Face.System.Result.RequireConnectServer_Result;

/**
 * 命令设备立刻重新连接服务器<br>
 * 让设备立即重新连接服务器，已连接时，立即断开，然后重新连接；<br>
 * 适用于 TCP Client 模式，UDP Client模式时，仅重发保活包。<br>
 */
public class RequireConnectServer extends Door8800Command {
    /**
     * 命令设备立刻重新连接服务器
     * @param parameter 连接参数
     */
    public  RequireConnectServer(CommandParameter parameter){
        _Parameter = parameter;
        CreatePacket(0x01, 0x30, 0x06);
    }

    @Override
    protected boolean _CommandStep(INConnectorEvent oEvent, Door8800PacketModel model) {
        boolean IsCommandComplete=false;
        if(CheckResponse_Cmd(model,1)){
            RequireConnectServer_Result result=new RequireConnectServer_Result();
            result.SetBytes(model.GetDatabuff());
            _Result=result;
            IsCommandComplete=  true;
        }
        RaiseCommandCompleteEvent(oEvent);
        return IsCommandComplete;
    }

    @Override
    protected void Release0() {

    }
}

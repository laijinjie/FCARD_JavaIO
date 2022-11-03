package Face.System;

import Door.Access.Command.CommandParameter;
import Door.Access.Connector.INConnectorEvent;
import Door.Access.Door8800.Command.Door8800Command;
import Door.Access.Door8800.Packet.Door8800PacketModel;
import Face.System.Result.ReadManageMenuPassword_Result;
import io.netty.buffer.ByteBufUtil;

/**
 * 读取管理密码
 */
public class ReadManageMenuPassword extends Door8800Command {
    /**
     * 初始化
     *
     * @param parameter 通讯参数
     */
    public ReadManageMenuPassword(CommandParameter parameter) {
        _Parameter = parameter;
        CreatePacket(0x01, 0x1D, 0x01);
    }

    @Override
    protected boolean _CommandStep(INConnectorEvent oEvent, Door8800PacketModel model) {

        if (CheckResponse_Cmd(model, 0x04)) {
            String pwd=ByteBufUtil.hexDump(model.GetDatabuff());
            ReadManageMenuPassword_Result result = new ReadManageMenuPassword_Result(pwd.replace("f",""));
            _Result = result;
        }
        RaiseCommandCompleteEvent(oEvent);
        return true;
    }



    @Override
    protected void Release0() {

    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Door.Access.Door8800.Command.Password;

import Door.Access.Command.CommandParameter;
import Door.Access.Connector.INConnectorEvent;
import Door.Access.Door8800.Command.Door8800Command;
import Door.Access.Door8800.Command.Password.Result.ReadPasswordDataBaseDetail_Result;
import Door.Access.Door8800.Packet.Door8800PacketModel;

/**
 * 读取开门密码存储详情
 *
 * @author F
 */
public class ReadPasswordDataBaseDetail extends Door8800Command {

    public ReadPasswordDataBaseDetail(CommandParameter par) {
        _Parameter = par;
        CreatePacket(0x05, 0x01);
    }

    @Override
    protected boolean _CommandStep(INConnectorEvent oEvent, Door8800PacketModel model) {
        if (CheckResponse_Cmd(model, 0x05, 0x01)) {
            ReadPasswordDataBaseDetail_Result result = new ReadPasswordDataBaseDetail_Result();
            result.SetBytes(model.GetDatabuff());
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

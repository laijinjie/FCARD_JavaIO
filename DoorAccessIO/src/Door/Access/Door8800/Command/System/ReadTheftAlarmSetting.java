/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Door.Access.Door8800.Command.System;

import Door.Access.Command.CommandParameter;
import Door.Access.Connector.INConnectorEvent;
import Door.Access.Door8800.Command.Data.TheftAlarmSetting;
import Door.Access.Door8800.Command.Door8800Command;
import Door.Access.Door8800.Command.System.Result.ReadTheftAlarmSetting_Result;
import Door.Access.Door8800.Packet.Door8800PacketModel;
import io.netty.buffer.ByteBuf;

/**
 * 智能防盗主机参数.<br>
 * 成功返回结果参考 {@link ReadTheftAlarmSetting_Result}
 *
 * @author 赖金杰
 */
public class ReadTheftAlarmSetting extends Door8800Command {

    public ReadTheftAlarmSetting(CommandParameter par) {
        _Parameter = par;
        CreatePacket(1, 0xA, 0x8E);
    }

    @Override
    protected void Release0() {
        return;
    }

    @Override
    protected boolean _CommandStep(INConnectorEvent oEvent, Door8800PacketModel model) {
        if (CheckResponse_Cmd(model, 1, 0xA, 0x8E, 0xD)) {
            ByteBuf buf = model.GetDatabuff();
            //设定返回值
            ReadTheftAlarmSetting_Result ret = new ReadTheftAlarmSetting_Result();
            _Result = ret;
            TheftAlarmSetting ts = new TheftAlarmSetting();
            ts.SetBytes(buf);
            ret.Setting = ts;
            RaiseCommandCompleteEvent(oEvent);
            return true;
        } else {
            return false;
        }
    }

}

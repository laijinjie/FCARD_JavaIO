/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Net.PC15.FC8800.Command.System;

import Net.PC15.Command.CommandParameter;
import Net.PC15.Connector.INConnectorEvent;
import Net.PC15.FC8800.Command.Data.TheftAlarmSetting;
import Net.PC15.FC8800.Command.FC8800Command;
import Net.PC15.FC8800.Command.System.Result.ReadTheftAlarmSetting_Result;
import Net.PC15.FC8800.Packet.FC8800PacketModel;
import io.netty.buffer.ByteBuf;

/**
 * 智能防盗主机参数.<br/>
 * 成功返回结果参考 {@link ReadTheftAlarmSetting_Result}
 *
 * @author 赖金杰
 */
public class ReadTheftAlarmSetting extends FC8800Command {

    public ReadTheftAlarmSetting(CommandParameter par) {
        _Parameter = par;
        CreatePacket(1, 0xA, 0x8E);
    }

    @Override
    protected void Release0() {
        return;
    }

    @Override
    protected boolean _CommandStep(INConnectorEvent oEvent, FC8800PacketModel model) {
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

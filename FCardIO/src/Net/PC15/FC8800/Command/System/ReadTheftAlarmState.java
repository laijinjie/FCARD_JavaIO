/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Net.PC15.FC8800.Command.System;

import Net.PC15.Command.CommandParameter;
import Net.PC15.Connector.INConnectorEvent;
import Net.PC15.FC8800.Command.FC8800Command;
import Net.PC15.FC8800.Command.System.Result.ReadTheftAlarmState_Result;
import Net.PC15.FC8800.Packet.FC8800PacketModel;
import io.netty.buffer.ByteBuf;

/**
 * 获取防盗主机布防状态.<br/>
 * 成功返回结果参考 {@link ReadTheftAlarmState_Result}
 *
 * @author 赖金杰
 */
public class ReadTheftAlarmState extends FC8800Command {

    public ReadTheftAlarmState(CommandParameter par) {
        _Parameter = par;
        CreatePacket(1, 0xE, 1);
    }

    @Override
    protected void Release0() {
        return;
    }

    @Override
    protected boolean _CommandStep(INConnectorEvent oEvent, FC8800PacketModel model) {
        if (CheckResponse_Cmd(model, 1, 0xE, 1, 2)) {
            ByteBuf buf = model.GetDatabuff();
            //设定返回值
            ReadTheftAlarmState_Result ret = new ReadTheftAlarmState_Result();
            _Result = ret;

            ret.TheftState = buf.readByte();//防盗主机布防状态
            ret.TheftAlarm = buf.readByte();//防盗主机报警状态
            RaiseCommandCompleteEvent(oEvent);
            return true;
        } else {
            return false;
        }
    }

}

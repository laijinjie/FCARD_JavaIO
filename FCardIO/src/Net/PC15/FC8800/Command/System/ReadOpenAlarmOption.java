/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Net.PC15.FC8800.Command.System;

import Net.PC15.Command.CommandParameter;
import Net.PC15.Connector.INConnectorEvent;
import Net.PC15.FC8800.Command.FC8800Command;
import Net.PC15.FC8800.Command.System.Result.ReadOpenAlarmOption_Result;
import Net.PC15.FC8800.Packet.FC8800PacketModel;
import io.netty.buffer.ByteBuf;

/**
 * 匪警报警功能参数.<br/>
 * 成功返回结果参考 {@link ReadOpenAlarmOption_Result}
 *
 * @author 赖金杰
 */
public class ReadOpenAlarmOption extends FC8800Command {

    public ReadOpenAlarmOption(CommandParameter par) {
        _Parameter = par;
        CreatePacket(1, 0xA, 0x86);
    }

    @Override
    protected void Release0() {
        return;
    }

    @Override
    protected boolean _CommandStep(INConnectorEvent oEvent, FC8800PacketModel model) {
        if (CheckResponse_Cmd(model, 1, 0xA, 0x86, 1)) {
            ByteBuf buf = model.GetDatabuff();
            //设定返回值
            ReadOpenAlarmOption_Result ret = new ReadOpenAlarmOption_Result();
            _Result = ret;

            ret.OpenAlarm = buf.readUnsignedByte();
            RaiseCommandCompleteEvent(oEvent);
            return true;
        } else {
            return false;
        }
    }

}

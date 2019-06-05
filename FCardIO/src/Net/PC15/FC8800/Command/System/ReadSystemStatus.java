/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Net.PC15.FC8800.Command.System;

import Net.PC15.Command.CommandParameter;
import Net.PC15.Connector.INConnectorEvent;
import Net.PC15.FC8800.Command.FC8800Command;
import Net.PC15.FC8800.Command.System.Result.ReadSystemStatus_Result;
import Net.PC15.FC8800.Packet.FC8800PacketModel;
import Net.PC15.Util.TimeUtil;
import io.netty.buffer.ByteBuf;

/**
 * 读取设备运行信息
 * <p>
 * 成功返回结果参考 {@link ReadSystemStatus_Result}
 *
 * @author 赖金杰
 */
public class ReadSystemStatus extends FC8800Command {

    public ReadSystemStatus(CommandParameter par) {
        _Parameter = par;

        CreatePacket(1, 9);
    }

    @Override
    protected void Release0() {
        return;
    }

    @Override
    protected boolean _CommandStep(INConnectorEvent oEvent, FC8800PacketModel model) {
        if (CheckResponse_Cmd(model, 1, 9, 0, 18)) {
            ByteBuf buf = model.GetDatabuff();

            //设定返回值
            ReadSystemStatus_Result rst = new ReadSystemStatus_Result();
            _Result = rst;
            rst.RunDay = buf.readUnsignedShort();
            rst.FormatCount = buf.readUnsignedShort();
            rst.RestartCount = buf.readUnsignedShort();
            rst.UPS = buf.readUnsignedByte();
            byte tmp = buf.readByte();
            rst.Temperature = buf.readUnsignedByte();
            if (tmp == 0) {
                rst.Temperature = -rst.Temperature;
            }
            byte[] btData = new byte[7];
            buf.readBytes(btData, 0, 7);
            btData[5] = btData[6];
            rst.StartTime = TimeUtil.BCDTimeToDate_ssmmhhddMMyy(btData);
            buf.readBytes(btData, 0, 2);
            rst.Voltage = Float.parseFloat(String.format("%d.%d", btData[0], btData[1]));

            RaiseCommandCompleteEvent(oEvent);
            return true;
        } else {
            return false;
        }
    }

}

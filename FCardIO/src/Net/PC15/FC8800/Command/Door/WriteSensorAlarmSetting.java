/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Net.PC15.FC8800.Command.Door;

import Net.PC15.FC8800.Command.Door.Parameter.WriteSensorAlarmSetting_Parameter;
import Net.PC15.FC8800.Command.FC8800Command;
import Net.PC15.Util.ByteUtil;
import io.netty.buffer.ByteBuf;

/**
 * 门磁报警功能<br/>
 * 当无有效开门验证时（远程开门、刷卡、密码、出门按钮），检测到门磁打开时就会报警。<br/>
 *
 * @author 赖金杰
 */
public class WriteSensorAlarmSetting extends FC8800Command {

    public WriteSensorAlarmSetting(WriteSensorAlarmSetting_Parameter par) {
        _Parameter = par;
        ByteBuf dataBuf = ByteUtil.ALLOCATOR.buffer(0xE2);
        dataBuf.writeByte(par.DoorNum);
        dataBuf.writeBoolean(par.Use);
        par.TimeGroup.GetBytes(dataBuf);

        CreatePacket(3, 0x10, 1, 0xE2, dataBuf);
    }

    @Override
    protected void Release0() {
    }

}

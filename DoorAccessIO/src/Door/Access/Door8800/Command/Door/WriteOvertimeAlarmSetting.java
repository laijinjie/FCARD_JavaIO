/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Door.Access.Door8800.Command.Door;

import Door.Access.Door8800.Command.Door.Parameter.WriteOvertimeAlarmSetting_Parameter;
import Door.Access.Door8800.Command.Door8800Command;
import Door.Access.Util.ByteUtil;
import io.netty.buffer.ByteBuf;

/**
 * 开门超时报警功能<br/>
 * 门磁打开超过一定时间后就会报警和发出提示语音和响声。<br/>
 *
 * @author 赖金杰
 */
public class WriteOvertimeAlarmSetting extends Door8800Command {

    public WriteOvertimeAlarmSetting(WriteOvertimeAlarmSetting_Parameter par) {
        _Parameter = par;
        ByteBuf dataBuf = ByteUtil.ALLOCATOR.buffer(5);
        dataBuf.writeByte(par.DoorNum);
        dataBuf.writeBoolean(par.Use);
        dataBuf.writeShort(par.Overtime);
        dataBuf.writeBoolean(par.Alarm);
        CreatePacket(3, 0xE, 1, 5, dataBuf);
    }

    @Override
    protected void Release0() {
    }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Door.Access.Door8800.Command.System;

import Door.Access.Door8800.Command.Door8800Command;
import Door.Access.Door8800.Command.System.Parameter.WriteTheftAlarmSetting_Parameter;
import io.netty.buffer.ByteBuf;

/**
 * 智能防盗主机参数
 *
 * @author 赖金杰
 */
public class WriteTheftAlarmSetting extends Door8800Command {

    public WriteTheftAlarmSetting(WriteTheftAlarmSetting_Parameter par) {
        _Parameter = par;

        ByteBuf dataBuf = par.Setting.GetBytes();
        CreatePacket(1, 0xA, 0xE, par.Setting.GetDataLen(), dataBuf);
    }

    @Override
    protected void Release0() {
        return;
    }

}

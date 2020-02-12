/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Door.Access.Door8800.Command.System;

import Door.Access.Door8800.Command.Door8800Command;
import Door.Access.Door8800.Command.System.Parameter.CloseAlarm_Parameter;
import Door.Access.Util.ByteUtil;
import io.netty.buffer.ByteBuf;

/**
 * 解除报警
 *
 * @author 赖金杰
 */
public class CloseAlarm extends Door8800Command {

    public CloseAlarm(CloseAlarm_Parameter par) {
        _Parameter = par;
        ByteBuf dataBuf = ByteUtil.ALLOCATOR.buffer(3);
        dataBuf.writeByte(par.Door);
        dataBuf.writeShort(par.Alarm.Alarm);
        CreatePacket(1, 0xD, 0, 3, dataBuf);
    }

    @Override
    protected void Release0() {
        return;
    }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Door.Access.Door8800.Command.System;

import Door.Access.Door8800.Command.Door8800Command;
import Door.Access.Door8800.Command.System.Parameter.WriteCardDeadlineTipDay_Parameter;
import Door.Access.Util.ByteUtil;
import io.netty.buffer.ByteBuf;

/**
 * 有效期即将过期提醒时间
 *
 * @author 赖金杰
 */
public class WriteCardDeadlineTipDay extends Door8800Command {

    public WriteCardDeadlineTipDay(WriteCardDeadlineTipDay_Parameter par) {
        _Parameter = par;

        ByteBuf dataBuf = ByteUtil.ALLOCATOR.buffer(1);
        dataBuf.writeByte(par.Day);
        CreatePacket(1, 0x15, 0, 1, dataBuf);
    }

    @Override
    protected void Release0() {
        return;
    }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Door.Access.Door8800.Command.System;

import Door.Access.Door8800.Command.Door8800Command;
import Door.Access.Door8800.Command.System.Parameter.WriteCardPeriodSpeak_Parameter;
import Door.Access.Util.ByteUtil;
import io.netty.buffer.ByteBuf;

/**
 * 卡片到期提示参数
 *
 * @author 赖金杰
 */
public class WriteCardPeriodSpeak extends Door8800Command {

    public WriteCardPeriodSpeak(WriteCardPeriodSpeak_Parameter par) {
        _Parameter = par;

        ByteBuf dataBuf = ByteUtil.ALLOCATOR.buffer(1);
        int iValue = 0;
        if (par.Use) {
            iValue = 1;
        }
        dataBuf.writeByte(iValue);
        CreatePacket(1, 0xA, 0x10, 1, dataBuf);
    }

    @Override
    protected void Release0() {
        return;
    }

}

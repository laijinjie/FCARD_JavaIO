/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Door.Access.Door8800.Command.System;

import Door.Access.Door8800.Command.Door8800Command;
import Door.Access.Door8800.Command.System.Parameter.WriteOpenAlarmOption_Parameter;
import Door.Access.Util.ByteUtil;
import io.netty.buffer.ByteBuf;

/**
 * 匪警报警功能参数
 *
 * @author 赖金杰
 */
public class WriteOpenAlarmOption extends Door8800Command {

    public WriteOpenAlarmOption(WriteOpenAlarmOption_Parameter par) {
        _Parameter = par;

        ByteBuf dataBuf = ByteUtil.ALLOCATOR.buffer(1);
        dataBuf.writeByte(par.Option);
        CreatePacket(1, 0xA, 6, 1, dataBuf);
    }

    @Override
    protected void Release0() {
        return;
    }

}

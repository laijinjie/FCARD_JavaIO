/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Door.Access.Door8800.Command.System;

import Door.Access.Door8800.Command.Door8800Command;
import Door.Access.Door8800.Command.System.Parameter.WriteRecordMode_Parameter;
import Door.Access.Util.ByteUtil;
import io.netty.buffer.ByteBuf;

/**
 * 记录存储方式.
 *
 * @author 赖金杰
 */
public class WriteRecordMode extends Door8800Command {

    public WriteRecordMode(WriteRecordMode_Parameter par) {
        _Parameter = par;

        ByteBuf dataBuf = ByteUtil.ALLOCATOR.buffer(1);
        dataBuf.writeByte(par.Mode);
        CreatePacket(1, 0xA, 1, 1, dataBuf);
    }

    @Override
    protected void Release0() {
        return;
    }

}

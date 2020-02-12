/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Door.Access.Door8800.Command.System;

import Door.Access.Door8800.Command.Door8800Command;
import Door.Access.Door8800.Command.System.Parameter.WriteDeadline_Parameter;
import Door.Access.Util.ByteUtil;
import io.netty.buffer.ByteBuf;

/**
 * 写入控制器有效期，剩余天数。
 *
 * @author 赖金杰
 */
public class WriteDeadline extends Door8800Command {

    public WriteDeadline(WriteDeadline_Parameter par) {
        _Parameter = par;
        int Deadline = par.GetDeadline();

        ByteBuf dataBuf = ByteUtil.ALLOCATOR.buffer(2);
        dataBuf.writeShort(Deadline);
        CreatePacket(1, 7, 1, 2, dataBuf);
    }

    @Override
    protected void Release0() {
        return;
    }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Door.Access.Door8800.Command.System;

import Door.Access.Door8800.Command.Door8800Command;
import Door.Access.Door8800.Command.System.Parameter.WriteBroadcast_Parameter;
import Door.Access.Util.ByteUtil;
import io.netty.buffer.ByteBuf;

/**
 * 语音段开关参数
 *
 * @author 赖金杰
 */
public class WriteBroadcast extends Door8800Command {

    public WriteBroadcast(WriteBroadcast_Parameter par) {
        _Parameter = par;

        ByteBuf dataBuf = ByteUtil.ALLOCATOR.buffer(10);
        dataBuf.writeBytes(par.Broadcast.Broadcast);
        CreatePacket(1, 0xA, 8, 10, dataBuf);
    }

    @Override
    protected void Release0() {
        return;
    }

}

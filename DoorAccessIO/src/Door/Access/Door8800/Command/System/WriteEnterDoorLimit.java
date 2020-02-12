/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Door.Access.Door8800.Command.System;

import Door.Access.Door8800.Command.Data.DoorLimit;
import Door.Access.Door8800.Command.Door8800Command;
import Door.Access.Door8800.Command.System.Parameter.WriteEnterDoorLimit_Parameter;
import Door.Access.Util.ByteUtil;
import io.netty.buffer.ByteBuf;

/**
 * 门内人数上限参数
 *
 * @author 赖金杰
 */
public class WriteEnterDoorLimit extends Door8800Command {

    public WriteEnterDoorLimit(WriteEnterDoorLimit_Parameter par) {
        _Parameter = par;

        ByteBuf dataBuf = ByteUtil.ALLOCATOR.buffer(36);
        DoorLimit limit=par.Limit;
        dataBuf.writeInt((int)limit.GlobalLimit);
        for (int i = 0; i < 4; i++) {
            dataBuf.writeInt((int)limit.DoorLimit[i]);
        }
        for (int i = 0; i < 4; i++) {
            dataBuf.writeInt((int)limit.DoorEnter[i]);
        }
        CreatePacket(1, 0xA, 0xC, 36, dataBuf);
    }

    @Override
    protected void Release0() {
        return;
    }

}

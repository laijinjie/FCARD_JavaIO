/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Door.Access.Door8800.Command.Door;

import Door.Access.Door8800.Command.Door.Parameter.WriteDoorWorkSetting_Parameter;
import Door.Access.Door8800.Command.Door8800Command;
import Door.Access.Util.ByteUtil;
import io.netty.buffer.ByteBuf;

/**
 * 门的工作模式，可设定多卡、首卡、常开 这三种特殊工作模式。
 *
 * @author 赖金杰
 */
public class WriteDoorWorkSetting extends Door8800Command {

    public WriteDoorWorkSetting(WriteDoorWorkSetting_Parameter par) {
        _Parameter = par;
        ByteBuf dataBuf = ByteUtil.ALLOCATOR.buffer(0xE5);
        dataBuf.writeByte(par.DoorNum);
        dataBuf.writeBoolean(par.Use);
        dataBuf.writeByte(par.DoorWorkType);
        dataBuf.writeByte(par.HoldDoorOption);
        dataBuf.writeByte(0);
        par.timeGroup.GetBytes(dataBuf);

        CreatePacket(3, 6, 1, 0xE5, dataBuf);
    }

    @Override
    protected void Release0() {
    }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Net.PC15.FC8800.Command.Door;

import Net.PC15.FC8800.Command.Door.Parameter.WriteAutoLockedSetting_Parameter;
import Net.PC15.FC8800.Command.FC8800Command;
import Net.PC15.Util.ByteUtil;
import io.netty.buffer.ByteBuf;

/**
 * 定时锁定门，可设定一周内任意时间的门锁定时段。
 *
 * @author 赖金杰
 */
public class WriteAutoLockedSetting extends FC8800Command {

    public WriteAutoLockedSetting(WriteAutoLockedSetting_Parameter par) {
        _Parameter = par;
        ByteBuf dataBuf = ByteUtil.ALLOCATOR.buffer(0xE2);
        dataBuf.writeByte(par.DoorNum);
        dataBuf.writeBoolean(par.Use);
        par.timeGroup.GetBytes(dataBuf);
        CreatePacket(3, 7, 0, 0xE2, dataBuf);
    }

    @Override
    protected void Release0() {
    }

}

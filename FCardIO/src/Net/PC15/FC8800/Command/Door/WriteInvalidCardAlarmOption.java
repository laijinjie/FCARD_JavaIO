/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Net.PC15.FC8800.Command.Door;

import Net.PC15.FC8800.Command.Door.Parameter.WriteInvalidCardAlarmOption_Parameter;
import Net.PC15.FC8800.Command.FC8800Command;
import Net.PC15.Util.ByteUtil;
import io.netty.buffer.ByteBuf;

/**
 * 未注册卡读卡时报警功能
 *
 * @author 赖金杰
 */
public class WriteInvalidCardAlarmOption extends FC8800Command {

    public WriteInvalidCardAlarmOption(WriteInvalidCardAlarmOption_Parameter par) {
        _Parameter = par;
        ByteBuf dataBuf = ByteUtil.ALLOCATOR.buffer(2);
        dataBuf.writeByte(par.DoorNum);
        dataBuf.writeBoolean(par.Use);
        CreatePacket(3, 0xA, 0, 2, dataBuf);
    }

    @Override
    protected void Release0() {
    }

}

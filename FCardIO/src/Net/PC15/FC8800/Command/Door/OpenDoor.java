/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Net.PC15.FC8800.Command.Door;

import Net.PC15.FC8800.Command.FC8800Command;
import Net.PC15.FC8800.Command.Door.Parameter.OpenDoor_Parameter;
import Net.PC15.Util.ByteUtil;
import io.netty.buffer.ByteBuf;

/**
 * 远程开门指令
 *
 * @author 赖金杰
 */
public class OpenDoor extends FC8800Command {

    public OpenDoor(OpenDoor_Parameter par) {
        _Parameter = par;
        ByteBuf dataBuf = ByteUtil.ALLOCATOR.buffer(5);

        if (par.IsCheckNum) {
            dataBuf.writeByte(par.CheckNum);
        }

        for (int i = 1; i < 5; i++) {
            dataBuf.writeByte(par.Door.GetDoor(i));
        }

        if (par.IsCheckNum) {
            CreatePacket(3, 3, 0x80, 5, dataBuf);
        } else {
            CreatePacket(3, 3, 0, 4, dataBuf);
        }

    }

    @Override
    protected void Release0() {
        return;
    }
}

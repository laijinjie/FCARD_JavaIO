/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Net.PC15.FC8800.Command.Door;

import Net.PC15.FC8800.Command.Door.Parameter.WriteAntiPassback_Parameter;
import Net.PC15.FC8800.Command.FC8800Command;
import Net.PC15.Util.ByteUtil;
import io.netty.buffer.ByteBuf;

/**
 * 防潜返<br/>
 * 刷卡进门后，必须刷卡出门才能再次刷卡进门。<br/>
 *
 * @author 赖金杰
 */
public class WriteAntiPassback extends FC8800Command {

    public WriteAntiPassback(WriteAntiPassback_Parameter par) {
        _Parameter = par;
        ByteBuf dataBuf = ByteUtil.ALLOCATOR.buffer(2);
        dataBuf.writeByte(par.DoorNum);
        dataBuf.writeBoolean(par.Use);
        CreatePacket(3, 0xC, 1, 2, dataBuf);
    }

    @Override
    protected void Release0() {
    }

}

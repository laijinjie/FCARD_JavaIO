/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Net.PC15.FC8800.Command.Door;

import Net.PC15.FC8800.Command.Door.Parameter.WriteAnyCardSetting_Parameter;
import Net.PC15.FC8800.Command.FC8800Command;
import Net.PC15.Util.ByteUtil;
import io.netty.buffer.ByteBuf;

/**
 * 全卡开门功能<br/>
 * 所有的卡都能开门，不需要权限首选注册，只要读卡器能识别就能开门。<br/>
 *
 * @author 赖金杰
 */
public class WriteAnyCardSetting extends FC8800Command {

    public WriteAnyCardSetting(WriteAnyCardSetting_Parameter par) {
        _Parameter = par;
        ByteBuf dataBuf = ByteUtil.ALLOCATOR.buffer(4);
        dataBuf.writeByte(par.DoorNum);
        dataBuf.writeBoolean(par.Use);
        dataBuf.writeBoolean(par.AutoSave);
        dataBuf.writeByte(par.AutoSaveTimeGroupIndex);

        CreatePacket(3, 0x11, 0, 4, dataBuf);
    }

    @Override
    protected void Release0() {
    }

}

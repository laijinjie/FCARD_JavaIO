/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Net.PC15.FC8800.Command.Door;

import Net.PC15.FC8800.Command.Door.Parameter.WritePushButtonSetting_Parameter;
import Net.PC15.FC8800.Command.FC8800Command;
import Net.PC15.Util.ByteUtil;
import io.netty.buffer.ByteBuf;

/**
 * 出门按钮功能<br/>
 * 可设定出门按钮的按下5秒后常开，还可以设定出门按钮的使用时段<br/>
 *
 * @author 赖金杰
 */
public class WritePushButtonSetting extends FC8800Command {

    public WritePushButtonSetting(WritePushButtonSetting_Parameter par) {
        _Parameter = par;
        ByteBuf dataBuf = ByteUtil.ALLOCATOR.buffer(0xE3);
        dataBuf.writeByte(par.DoorNum);
        dataBuf.writeBoolean(par.Use);
        dataBuf.writeBoolean(par.NormallyOpen);
        par.TimeGroup.GetBytes(dataBuf);
                
        CreatePacket(3, 0xF, 1, 0xE3, dataBuf);
    }

    @Override
    protected void Release0() {
    }

}

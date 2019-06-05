/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Net.PC15.FC8800.Command.System;

import Net.PC15.FC8800.Command.FC8800Command;
import Net.PC15.FC8800.Command.System.Parameter.WriteKeyboard_Parameter;
import Net.PC15.Util.ByteUtil;
import io.netty.buffer.ByteBuf;

/**
 * 读卡器密码键盘启用功能开关
 *
 * @author 赖金杰
 */
public class WriteKeyboard extends FC8800Command {

    public WriteKeyboard(WriteKeyboard_Parameter par) {
        _Parameter = par;

        ByteBuf dataBuf = ByteUtil.ALLOCATOR.buffer(1);
        dataBuf.writeByte(par.Keyboard);
        CreatePacket(1, 0xA, 2, 1, dataBuf);
    }

    @Override
    protected void Release0() {
        return;
    }

}

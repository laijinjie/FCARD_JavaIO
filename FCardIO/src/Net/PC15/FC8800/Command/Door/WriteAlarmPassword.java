/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Net.PC15.FC8800.Command.Door;

import Net.PC15.FC8800.Command.Door.Parameter.WriteAlarmPassword_Parameter;
import Net.PC15.FC8800.Command.FC8800Command;
import Net.PC15.Util.ByteUtil;
import Net.PC15.Util.StringUtil;
import io.netty.buffer.ByteBuf;

/**
 * 胁迫报警功能<br/>
 * 功能开启后，在密码键盘读卡器上输入特定密码后就会报警；<br/>
 *
 * @author 赖金杰
 */
public class WriteAlarmPassword extends FC8800Command {

    public WriteAlarmPassword(WriteAlarmPassword_Parameter par) {
        _Parameter = par;
        ByteBuf dataBuf = ByteUtil.ALLOCATOR.buffer(7);
        dataBuf.writeByte(par.DoorNum);
        dataBuf.writeBoolean(par.Use);
        String pwd = par.GetPassword();
        if (StringUtil.IsNullOrEmpty(pwd)) {
            pwd = NULLPassword;
        }else{
            pwd=StringUtil.FillString(pwd, 8, "F");
        }
        StringUtil.HextoByteBuf(pwd, dataBuf);
        dataBuf.writeByte(par.AlarmOption);
        
        CreatePacket(3, 0xB, 0, 7, dataBuf);
    }

    @Override
    protected void Release0() {
    }

}

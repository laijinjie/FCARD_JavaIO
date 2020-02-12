/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Door.Access.Door8800.Command.Data;

import Door.Access.Data.INData;
import Door.Access.Util.ByteUtil;
import Door.Access.Util.StringUtil;
import io.netty.buffer.ByteBuf;

/**
 * 防盗报警参数
 *
 * @author 赖金杰
 */
public class TheftAlarmSetting implements INData {

    @Override
    public int GetDataLen() {
        return 0x0D;
    }

    @Override
    public void SetBytes(ByteBuf data) {
        //data.markReaderIndex();
        
        Use = data.readBoolean();
        InTime = data.readUnsignedByte();
        OutTime = data.readUnsignedByte();
        byte[] btPwd = new byte[4];
        data.readBytes(btPwd, 0, 4);
        BeginPassword = ByteUtil.ByteToHex(btPwd);

        data.readBytes(btPwd, 0, 4);
        ClosePassword = ByteUtil.ByteToHex(btPwd);

        AlarmTime = data.readUnsignedShort();
        //data.resetReaderIndex();
        return;
    }

    @Override
    public ByteBuf GetBytes() {
        ByteBuf buf = ByteUtil.ALLOCATOR.buffer(GetDataLen());
        buf.writeBoolean(Use);
        buf.writeByte(InTime);
        buf.writeByte(OutTime);
        BeginPassword = StringUtil.FillHexString(BeginPassword, 8, "F", false);
        ClosePassword = StringUtil.FillHexString(ClosePassword, 8, "F", false);
        long pwd = Long.parseLong(BeginPassword, 16);
        buf.writeInt((int)pwd);
        pwd = Long.parseLong(ClosePassword, 16);
        buf.writeInt((int)pwd);
        buf.writeShort(AlarmTime);
        return buf;
    }

    /**
     * 启用；0--不启用；1--启用
     */
    public boolean Use;
    /**
     * 进入延迟；单位：秒，取值：1-255
     */
    public int InTime;
    /**
     * 退出延迟；单位：秒，取值：1-255
     */
    public int OutTime;
    /**
     * 布防密码；8个数字。空补F。例如密码：23412；表达为：0xFFF23412
     */
    public String BeginPassword;
    /**
     * 撤防密码；8个数字。空补F。例如密码：23412；表达为：0xFFF23412
     */
    public String ClosePassword;

    /**
     * 报警时长；单位：秒 ,取值：0-65535
     */
    public int AlarmTime;

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Door.Access.Door89H.Command.Data;

import Door.Access.Util.ByteUtil;
import Door.Access.Util.StringUtil;
import Door.Access.Util.TimeUtil;
import io.netty.buffer.ByteBuf;

import java.util.Calendar;

/**
 * @author F
 */
public class PasswordDetail extends Door.Access.Door8800.Command.Data.PasswordDetail {

    public PasswordDetail() {
        Expiry = Calendar.getInstance();
        OpenTimes= 10;
    }

    /**
     * 开门次数
     */
    public int OpenTimes;
    /**
     * 有效期
     */
    public Calendar Expiry;

    @Override
    public void SetBytes(ByteBuf buf) {
        Door = buf.readByte();
        byte[] btData = new byte[4];
        buf.readBytes(btData, 0, 4);
        Password = ByteUtil.ByteToHex(btData);
        OpenTimes = buf.readUnsignedShort();
        byte[] btTime = new byte[6];
        buf.readBytes(btTime, 0, 5);
        Expiry = TimeUtil.BCDTimeToDate_yyMMddhhmm(btTime);
    }

    @Override
    public void GetBytes(ByteBuf buf) {
        buf.writeByte(Door);
        Password = StringUtil.FillHexString(Password, 8, "F", true);
        long pwd = Long.parseLong(Password, 16);
        buf.writeInt((int) pwd);
        buf.writeShort(OpenTimes);
        byte[] btTime = new byte[6];
        TimeUtil.DateToBCD_yyMMddhhmm(btTime, Expiry);
        buf.writeBytes(btTime, 0, 5);
    }
}

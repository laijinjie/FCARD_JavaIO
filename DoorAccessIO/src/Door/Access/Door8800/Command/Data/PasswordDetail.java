/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Door.Access.Door8800.Command.Data;

import Door.Access.Util.ByteUtil;
import Door.Access.Util.StringUtil;
import io.netty.buffer.ByteBuf;

/**
 *
 * @author F
 */
public class PasswordDetail {

    /**
     * 开门权限<br>
     * 1-4门的开门权限；false--无权，true--有权开门<br>
     * bit0 -- 1门的权限<br>
     * bit1 -- 2门的权限<br>
     * bit2 -- 3门的权限<br>
     * bit3 -- 4门的权限<br>
     */
    protected int Door;

    public String Password;

    /**
     * 获取指定门是否有权限
     *
     * @param iDoor 门号，取值范围：1-4
     * @return true 有权限，false 无权限。
     */
    public boolean GetDoor(int iDoor) {
        if (iDoor < 0 || iDoor > 4) {

            throw new IllegalArgumentException("Door 1-4");
        }
        iDoor -= 1;

        int iBitIndex = iDoor % 8;
        int iMaskValue = (int) Math.pow(2, iBitIndex);
        int iByteValue = Door & iMaskValue;
        if (iBitIndex > 0) {
            iByteValue = iByteValue >> (iBitIndex);
        }
        return iByteValue == 1;
    }

    /**
     * 设置指定门是否有权限
     *
     * @param iDoor 门号，取值范围：1-4
     * @param bUse true 有权限，false 无权限。
     */
    public void SetDoor(int iDoor, boolean bUse) {
        if (iDoor < 0 || iDoor > 4) {

            throw new IllegalArgumentException("Door 1-4");
        }

        if (bUse == GetDoor(iDoor)) {
            return;
        }

        iDoor -= 1;
        int iBitIndex = iDoor % 8;
        int iMaskValue = (int) Math.pow(2, iBitIndex);
        if (bUse) {
            Door = Door | iMaskValue;
        } else {
            Door = Door ^ iMaskValue;
        }
    }

    public void SetBytes(ByteBuf buf) {
        Door = buf.readByte();
        byte[] btData = new byte[4];
        buf.readBytes(btData, 0, 4);
        Password = ByteUtil.ByteToHex(btData);
    }

    public void GetBytes(ByteBuf buf) {
        buf.writeByte(Door);
        Password = StringUtil.FillHexString(Password, 8, "F", true);
        long pwd = Long.parseLong(Password, 16);
        buf.writeInt((int) pwd);
    }
}

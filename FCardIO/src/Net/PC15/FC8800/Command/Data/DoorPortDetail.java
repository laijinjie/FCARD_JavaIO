/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Net.PC15.FC8800.Command.Data;

import Net.PC15.Util.ByteUtil;

/**
 * 门端口参数详情
 *
 * @author 赖金杰
 */
public class DoorPortDetail {

    public byte DoorPort[];
    public final short DoorMax;

    public DoorPortDetail(short iDoorMax) {
        DoorMax = iDoorMax;
        DoorPort = new byte[iDoorMax];
    }

    /**
     * 设置门端口参数
     *
     * @param iDoor 门号
     * @param iValue 参数值
     */
    public void SetDoor(int iDoor, int iValue) {
        if (iDoor >= 1 || iDoor <= 4) {
            DoorPort[iDoor - 1] = (byte) iValue;
        }
    }

    /**
     * 获取门端口参数
     *
     * @param iDoor 门号
     * @return 参数值
     */
    public int GetDoor(int iDoor) {
        if (iDoor >= 1 || iDoor <= 4) {
            return ByteUtil.uByte(DoorPort[iDoor - 1]);
        }
        return 0;
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Net.PC15.FC8800.Command.Data;

/**
 * 语音段开关
 *
 * @author 赖金杰
 */
public class BroadcastDetail {

    public final byte[] Broadcast;

    public BroadcastDetail() {
        Broadcast = new byte[10];
    }

    public BroadcastDetail(byte[] data) {
        if (data.length != 10) {
            throw new IllegalArgumentException("data.length != 10");
        }

        Broadcast = data;
    }

    /**
     * 获取指定序号的开关状态
     *
     * @param iIndex 取值范围 1-80
     * @return 开关状态 开关true 表示启用，false 表示禁用
     */
    public boolean GetValue(int iIndex) {
        if (iIndex <= 0 || iIndex > 80) {
            throw new IllegalArgumentException("iIndex= 1 -- 80");
        }
        iIndex -= 1;
        //计算索引所在的字节位置
        int iByteIndex = iIndex / 8;
        int iBitIndex = iIndex % 8;
        int iByteValue = Broadcast[iByteIndex] & 0x000000ff;
        int iMaskValue =  (int) Math.pow(2, iBitIndex);
        iByteValue = iByteValue & iMaskValue;
        if (iBitIndex > 0) {
            iByteValue = iByteValue >> (iBitIndex );
        }
        return iByteValue == 1;

    }

    /**
     * 设置指定序号的开关状态
     *
     * @param iIndex 取值范围 1-80
     * @param bUse 开关状态 开关true 表示启用，false 表示禁用
     */
    public void SetValue(int iIndex, boolean bUse) {
        if (iIndex <= 0 || iIndex > 80) {
            throw new IllegalArgumentException("iIndex= 1 -- 80");
        }
        if (bUse == GetValue(iIndex)) {
            return;
        }
        iIndex -= 1;
        int iByteIndex = iIndex / 8;
        int iBitIndex = iIndex % 8;
        int iByteValue = Broadcast[iByteIndex] & 0x000000ff;
        int iMaskValue =  (int) Math.pow(2, iBitIndex);
        if (bUse) {
            iByteValue = iByteValue | iMaskValue;
        } else {
            iByteValue = iByteValue ^ iMaskValue;
        }

        Broadcast[iByteIndex] = (byte) iByteValue;

    }

}

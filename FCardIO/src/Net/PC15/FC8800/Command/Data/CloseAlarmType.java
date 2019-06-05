/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Net.PC15.FC8800.Command.Data;

/**
 * 报警类型--用于解除报警<br/>
 * <ul>
 * <li>00--非法卡报警</li>
 * <li>01--门磁报警</li>
 * <li>02--胁迫报警</li>
 * <li>03--开门超时报警</li>
 * <li>04--黑名单报警</li>
 * <li>05--匪警报警</li>
 * <li>06--防盗主机报警</li>
 * <li>07--消防报警</li>
 * <li>08--烟雾报警</li>
 * <li>09--关闭电锁出错报警</li>
 * <li>10--防拆报警</li>
 * <li>11--强制关锁报警</li>
 * <li>12--强制开锁报警</li>
 * <li>9-12 为一体锁或一体机报警类型</li>
 * </ul>
 *
 * @author 赖金杰
 */
public class CloseAlarmType {

    public int Alarm;

    /**
     * 获取指定序号的报警状态
     *
     * @param iIndex 取值范围 1-80
     * @return 是否需要关闭报警
     */
    public boolean GetValue(int iIndex) {
        if (iIndex < 0 || iIndex > 12) {
            throw new IllegalArgumentException("iIndex= 0 -- 12");
        }
        int iMaskValue = (int) Math.pow(2, iIndex);
        int iByteValue = Alarm & iMaskValue;
        if (iIndex > 0) {
            iByteValue = iByteValue >> iIndex;
        }
        return iByteValue == 1;

    }

    /**
     * 设置指定序号的报警状态
     *
     * @param iIndex 取值范围 1-80
     * @param bUse 是否需要关闭报警
     */
    public void SetValue(int iIndex, boolean bUse) {
        if (iIndex < 0 || iIndex > 12) {
            throw new IllegalArgumentException("iIndex= 0 -- 12");
        }
        int iMaskValue = (int) Math.pow(2, iIndex);
        if (bUse) {
            Alarm = Alarm | iMaskValue;
        } else {
            Alarm = Alarm ^ iMaskValue;
        }
    }
}

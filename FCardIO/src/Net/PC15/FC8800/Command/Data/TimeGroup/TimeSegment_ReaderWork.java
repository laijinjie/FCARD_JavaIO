/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Net.PC15.FC8800.Command.Data.TimeGroup;

import Net.PC15.Util.ByteUtil;
import io.netty.buffer.ByteBuf;

/**
 * 读卡认证方式中的时段详情
 *
 * @author 赖金杰
 */
public class TimeSegment_ReaderWork extends TimeSegment {

    /**
     * 验证方式的枚举
     */
    public enum ReaderWorkType {
        /**
         * 仅读卡
         */
        OnlyCard(0),
        /**
         * 仅密码
         */
        OnlyPassword(1),
        /**
         * 读卡加密码
         */
        CardAndPassword(2),
        /**
         * 手动输入卡号+密码
         */
        InputCardAndPassword(3);
        
        private final int value;

        //构造器默认也只能是private, 从而保证构造函数只能在内部使用
        ReaderWorkType(int value) {
            this.value = value;
        }

        /**
         * 获取枚举值
         *
         * @return 类型代码
         */
        public int getValue() {
            return value;
        }
    }

    /**
     * 验证工作方式<br/>
     * <ul>
     * <li>Bit0 &emsp; 只读卡           </li>
     * <li>Bit1 &emsp; 只密码           </li>
     * <li>Bit2 &emsp; 读卡加密码       </li>
     * <li>Bit3 &emsp; 手动输入卡号+密码</li>
     * </ul>
     */
    protected int mWortType;
    
    public TimeSegment_ReaderWork() {
        super();
        mWortType = 0;
    }

    /**
     * 获取指定验证方式的开关状态
     *
     * @param type 验证方式的类型
     * @return 开关状态 开关true 表示启用，false 表示禁用
     */
    public boolean GetWorkType(ReaderWorkType type) {

        //计算索引所在的字节位置
        int iBitIndex = type.getValue() % 8;
        int iMaskValue = (int) Math.pow(2, iBitIndex);
        int iByteValue = mWortType & iMaskValue;
        if (iBitIndex > 0) {
            iByteValue = iByteValue >> (iBitIndex);
        }
        return iByteValue == 1;
        
    }

    /**
     * 设置指定验证方式的开关状态
     *
     * @param type 验证方式的类型
     * @param bUse 开关状态 开关true 表示启用，false 表示禁用
     */
    public void SetWorkType(ReaderWorkType type, boolean bUse) {
        
        if (bUse == GetWorkType(type)) {
            return;
        }
        int iBitIndex = type.getValue() % 8;
        int iMaskValue = (int) Math.pow(2, iBitIndex);
        if (bUse) {
            mWortType = mWortType | iMaskValue;
        } else {
            mWortType = mWortType ^ iMaskValue;
        }
    }

    /**
     * 将对象写入到字节缓冲区
     *
     * @param bBuf
     */
    public void GetBytes(ByteBuf bBuf) {
        super.GetBytes(bBuf);
        bBuf.writeByte(ByteUtil.ByteToBCD((byte) mWortType));
    }

    /**
     * 从字节缓冲区中生成一个对象
     *
     * @param bBuf
     */
    public void SetBytes(ByteBuf bBuf) {
        super.SetBytes(bBuf);
        
        mWortType = bBuf.readByte();
    }
    
}

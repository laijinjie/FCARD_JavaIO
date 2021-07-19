/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Door.Access.Door8800.Command.Data.TimeGroup;

import Door.Access.Util.ByteUtil;
import io.netty.buffer.ByteBuf;

/**
 * 表示一个时段，开始时间和结束时间
 *
 * @author 赖金杰
 */
public class TimeSegment {

    protected short[] mBeginTime;
    protected short[] mEndTime;

    public TimeSegment() {
        mBeginTime = new short[2];
        mEndTime = new short[2];
    }

    /**
     * 设置开始时间
     *
     * @param Hour 开始时间的小时部分 取值范围 0-23
     * @param Minute 开始时间的分钟部分 取值范围 0-59
     */
    public void SetBeginTime(int Hour, int Minute) {
        CheckTime(Hour, Minute);
        mBeginTime[0] = (short) Hour;
        mBeginTime[1] = (short) Minute;
    }

    /**
     * 获取开始时间
     *
     * @param time 开始时间返回的数组，传入时需要数组保持有2个元素 即 new short[2]
     */
    public void GetBeginTime(short[] time) {
        time[0] = mBeginTime[0];
        time[1] = mBeginTime[1];
    }

    /**
     * 设置结束时间
     *
     * @param Hour 开始时间的小时部分 取值范围 0-23
     * @param Minute 开始时间的分钟部分 取值范围 0-59
     */
    public void SetEndTime(int Hour, int Minute) {
        CheckTime(Hour, Minute);
        mEndTime[0] = (short) Hour;
        mEndTime[1] = (short) Minute;
    }

    /**
     * 获取开始时间
     *
     * @param time 开始时间返回的数组，传入时需要数组保持有2个元素 即 new short[2]
     */
    public void GetEndTime(short[] time) {
        time[0] = mEndTime[0];
        time[1] = mEndTime[1];
    }

    @Override
    public String toString() {
        StringBuilder buf = new StringBuilder(15);
        buf.append(String.format("%02d", mBeginTime[0]));
        buf.append(":");
        buf.append(String.format("%02d", mBeginTime[1]));
        buf.append(" - ");
        buf.append(String.format("%02d", mEndTime[0]));
        buf.append(":");
        buf.append(String.format("%02d", mEndTime[1]));
        return buf.toString();
    }

    private void CheckTime(int Hour, int Minute) {
        if (Hour < 0 || Hour > 23) {
            throw new IllegalArgumentException("Hour -- 0-23");
        }

        if (Minute < 0 || Minute > 59) {
            throw new IllegalArgumentException("Minute -- 0-59");
        }
    }

    /**
     * 将对象写入到字节缓冲区
     *
     * @param bBuf
     */
    public void GetBytes(ByteBuf bBuf) {

        bBuf.writeByte(ByteUtil.ByteToBCD((byte) mBeginTime[0]));
        bBuf.writeByte(ByteUtil.ByteToBCD((byte) mBeginTime[1]));
        bBuf.writeByte(ByteUtil.ByteToBCD((byte) mEndTime[0]));
        bBuf.writeByte(ByteUtil.ByteToBCD((byte) mEndTime[1]));
    }

    /**
     * 从字节缓冲区中生成一个对象
     *
     * @param bBuf
     */
    public void SetBytes(ByteBuf bBuf) {
        mBeginTime[0] = ByteUtil.BCDToByte(bBuf.readByte());
        mBeginTime[1] = ByteUtil.BCDToByte(bBuf.readByte());

        mEndTime[0] = ByteUtil.BCDToByte(bBuf.readByte());
        mEndTime[1] = ByteUtil.BCDToByte(bBuf.readByte());
    }

}

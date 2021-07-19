/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Door.Access.Door8800.Command.Data.TimeGroup;

import Door.Access.Util.ByteUtil;
import io.netty.buffer.ByteBuf;

/**
 * 表示一天的时段 ,一天可以包含多个时段
 *
 * @author 赖金杰
 */
public class DayTimeGroup {

    protected TimeSegment[] mSegment;

    /**
     * 初始化天时段
     *
     * @param SegmentCount 一天中的时段数量
     */
    public DayTimeGroup(int SegmentCount) {
        SetSegmentCount(SegmentCount);
    }

    /**
     * 设置一天中包含的时段数量
     *
     * @param SegmentCount 时段数量
     */
    public void SetSegmentCount(int SegmentCount) {
        mSegment = new TimeSegment[SegmentCount];
        for (int i = 0; i < SegmentCount; i++) {
            mSegment[i] = new TimeSegment();
        }
    }

    /**
     * 获取一天中包含的时段数量
     *
     * @return 时段数量
     */
    public int GetSegmentCount() {
        if (mSegment == null) {
            return 0;

        }
        return mSegment.length;
    }

    /**
     * 获取一个时段，进行操作
     *
     * @param iIndex 此时段在这一天当中的索引号，索引从0开始
     * @return 时间段
     */
    public TimeSegment GetItem(int iIndex) {
        if (iIndex < 0 || iIndex > GetSegmentCount()) {
            throw new IllegalArgumentException("iIndex<0 || iIndex > GetSegmentCount()");
        }
        return mSegment[iIndex];
    }

    /**
     * 设置一个时段
     *
     * @param ts 时段
     * @param index 此时段在这一天当中的索引号，索引从0开始
     * @return
     */
    public boolean SetItem(TimeSegment ts, int index) {
        if (index < 0 || index > GetSegmentCount()) {
            throw new IllegalArgumentException("iIndex<0 || iIndex > GetSegmentCount()");
        }
        mSegment[index] = ts;
        return true;
    }

    /**
     * 将对象写入到字节缓冲区
     *
     * @param bBuf
     */
    public void GetBytes(ByteBuf bBuf) {
        int iCount = GetSegmentCount();
        for (int i = 0; i < iCount; i++) {
            mSegment[i].GetBytes(bBuf);
        }

    }

    /**
     * 从字节缓冲区中生成一个对象
     *
     * @param bBuf
     */
    public void SetBytes(ByteBuf bBuf) {
        int iCount = GetSegmentCount();
        for (int i = 0; i < iCount; i++) {
            mSegment[i].SetBytes(bBuf);
            if (bBuf.readableBytes() == 0) {
                return;
            }
        }
    }

}

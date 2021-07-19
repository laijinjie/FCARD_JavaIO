/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Door.Access.Door8800.Command.Data.TimeGroup;

import Door.Access.Data.INData;
import Door.Access.Door8800.Command.Data.E_WeekDay;
import Door.Access.Util.ByteUtil;
import io.netty.buffer.ByteBuf;

/**
 * 表示一个完整时段，一个时段里包含7天
 *
 * @author 赖金杰
 */
public class WeekTimeGroup implements INData {

    protected DayTimeGroup[] mDay;
    protected int mIndex;
    protected int DaySegmentCount;

    /**
     * 初始化一个周时段
     *
     * @param iDaySegmentCount 一天中的时段数量
     */
    public WeekTimeGroup(int iDaySegmentCount) {

        DaySegmentCount = iDaySegmentCount;
        CreateDayTimeGroup();
    }

    /**
     * 创建一周中的天时段
     */
    protected void CreateDayTimeGroup() {
        mDay = new DayTimeGroup[7];
        for (int i = 0; i < 7; i++) {
            mDay[i] = new DayTimeGroup(DaySegmentCount);
        }
    }

    public WeekTimeGroup(int iDaySegmentCount, int index) {
        this(iDaySegmentCount);
        mIndex = index;
    }

    /**
     * 获取在周时段列表中的索引号
     *
     * @return 索引号 1-64
     */
    public int GetIndex() {
        return mIndex;
    }

    /**
     * 设定在周时段列表中的索引号
     *
     * @param index 索引号 1-64
     */
    public void SetIndex(int index) {
        mIndex = index;
    }

    public DayTimeGroup GetItem(E_WeekDay week) {
        return mDay[week.getValue()];
    }

    @Override
    public int GetDataLen() {
        return 7 * DaySegmentCount * 4;
    }

    @Override
    public void SetBytes(ByteBuf data) {
        SetBytes(E_WeekDay.Monday, data);
    }
     public void SetWeekTimeGroupBytes(ByteBuf data) {

        SetBytes(E_WeekDay.Monday, data);
    }

    /**
     * 从缓冲区中获取值并初始化周时段
     *
     * @param FistWeek 一周的第一天
     * @param data
     */
    public void SetBytes(E_WeekDay FistWeek, ByteBuf data) {
        int WeekList[] = new int[7];
        GetWeekList(FistWeek, WeekList);
        for (int i = 0; i < 7; i++) {
            mDay[WeekList[i]].SetBytes(data);
        }
    }

    protected void GetWeekList(E_WeekDay FistWeek, int[] WeekList) {
        int lBeginIndex = FistWeek.getValue();
        int iEndIndex = 6 - lBeginIndex;
        for (int i = 0; i <= iEndIndex; i++) {
            WeekList[i] = lBeginIndex + i;
        }
        if (lBeginIndex != 0) {
            iEndIndex += 1;
            lBeginIndex = 0;
            for (int i = iEndIndex; i <= 6; i++) {
                WeekList[i] = lBeginIndex;
                lBeginIndex += 1;
            }
        }
    }

    /**
     * 没有实现此函数，请不要调用
     *
     * @return null
     */
    @Override
    public ByteBuf GetBytes() {
        return null;
    }

    /**
     * 使用从周一为一周的第一天进行排序的缓冲区获取时段信息
     * @param data 
     */
    public void GetBytes(ByteBuf data) {
        GetBytes(E_WeekDay.Monday, data);
    }

    public void GetBytes(E_WeekDay FistWeek, ByteBuf data) {
        int WeekList[] = new int[7];
        GetWeekList(FistWeek, WeekList);
        for (int i = 0; i < 7; i++) {
            mDay[WeekList[i]].GetBytes(data);
        }
    }

    /**
     * 克隆一个周时段
     *
     * @return
     */
    public WeekTimeGroup Clone() {
        WeekTimeGroup w = new WeekTimeGroup(DaySegmentCount);
        ByteBuf bBuf = ByteUtil.ALLOCATOR.buffer(DaySegmentCount * 4);
        for (int i = 0; i < 10; i++) {
            mDay[i].GetBytes(bBuf);
            w.mDay[i].SetBytes(bBuf);
            bBuf.clear();
        }
        return w;
    }

}

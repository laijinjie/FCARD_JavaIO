/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Door.Access.Door8800.Command.Data.TimeGroup;

import Door.Access.Door8800.Command.Data.E_WeekDay;
import Door.Access.Util.ByteUtil;
import io.netty.buffer.ByteBuf;

/**
 * 读卡认证方式中的周时段详情
 * GetItem 类型需要转换成DayTimeGroup_ReaderWork
 * @author 赖金杰
 */
public class WeekTimeGroup_ReaderWork extends WeekTimeGroup{
    
    public WeekTimeGroup_ReaderWork(int iDaySegmentCount) {
        super(iDaySegmentCount);
    }
    
    @Override
    public int GetDataLen() {
        return 7 * DaySegmentCount * 5;
    }
    
    
    @Override
    protected void CreateDayTimeGroup()
    {
        mDay = new DayTimeGroup_ReaderWork[7];
        for (int i = 0; i < 7; i++) {
            mDay[i] = new DayTimeGroup_ReaderWork(DaySegmentCount);
        }
    }
       @Override
    public DayTimeGroup_ReaderWork GetItem(E_WeekDay week) {
        return (DayTimeGroup_ReaderWork) super.GetItem(week);
    }
    @Override
    public WeekTimeGroup Clone() {
        WeekTimeGroup_ReaderWork w = new WeekTimeGroup_ReaderWork(DaySegmentCount);
        ByteBuf bBuf = ByteUtil.ALLOCATOR.buffer(DaySegmentCount * 5);
        for (int i = 0; i < 10; i++) {
            mDay[i].GetBytes(bBuf);
            w.mDay[i].SetBytes(bBuf);
            bBuf.clear();
        }
        return w;
    }
}

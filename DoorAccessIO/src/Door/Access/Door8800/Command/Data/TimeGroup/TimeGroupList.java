/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Door.Access.Door8800.Command.Data.TimeGroup;

/**
 * 周时段，包含64个WeekTimeGroup
 *
 * @author 赖金杰
 */
public class TimeGroupList {

    protected WeekTimeGroup[] mWeekTimeGroup;
    protected int DaySegmentCount;

    public TimeGroupList(int iDaySegmentCount) {
        mWeekTimeGroup = new WeekTimeGroup[64];
        for (int i = 0; i < 64; i++) {
            mWeekTimeGroup[i] = new WeekTimeGroup(iDaySegmentCount, (i + 1));
        }
    }
    
    public int Count()
    {
        return 64;
    }
    
    public WeekTimeGroup GetItem(int iIndex)
    {
        return mWeekTimeGroup[iIndex];
    }
}

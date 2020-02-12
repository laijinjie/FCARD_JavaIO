/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Door.Access.Door8800.Command.Data.TimeGroup;

/**
 * 读卡认证方式中的每日时段详情
 * @author 赖金杰
 */
public class DayTimeGroup_ReaderWork extends DayTimeGroup {

    public DayTimeGroup_ReaderWork(int SegmentCount) {
        super(SegmentCount);
    }

    /**
     * 设置一天中包含的时段数量
     *
     * @param SegmentCount 时段数量
     */
    @Override
    public void SetSegmentCount(int SegmentCount) {
        mSegment = new TimeSegment_ReaderWork[SegmentCount];
        for (int i = 0; i < SegmentCount; i++) {
            mSegment[i] = new TimeSegment_ReaderWork();
        }
    }

}

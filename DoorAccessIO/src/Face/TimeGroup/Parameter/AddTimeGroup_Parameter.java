package Face.TimeGroup.Parameter;

import Door.Access.Command.CommandDetail;
import Door.Access.Command.CommandParameter;
import Door.Access.Door8800.Command.Data.TimeGroup.WeekTimeGroup;


import java.util.ArrayList;

public class AddTimeGroup_Parameter extends CommandParameter {
    /**
     * 开门时段列表（64个开门时段）
     */
    public ArrayList<WeekTimeGroup> ListWeekTimeGroup;

    /**
     * 初始化对象
     *
     * @param detail         命令详情
     * @param weekTimeGroups 开门时段列表 （64个开门时段）
     */
    public AddTimeGroup_Parameter(CommandDetail detail, ArrayList<WeekTimeGroup> weekTimeGroups) {
        super(detail);
        ListWeekTimeGroup = weekTimeGroups;
        checkedParameter();
    }

    /**
     * 缓冲区大小
     * @return
     */
    public int DataLen() {
        return 255;
    }

    /**
     * 检查开门时段
     */
    private void checkedParameter() {
        if (ListWeekTimeGroup == null || ListWeekTimeGroup.size() != 64) {
            throw new UnsupportedOperationException("ListWeekTimeGroup.Count Error!");
        }
    }
}

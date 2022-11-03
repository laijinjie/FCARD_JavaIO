package Face.TimeGroup.Result;

import Door.Access.Command.INCommandResult;
import Door.Access.Door8800.Command.Data.TimeGroup.WeekTimeGroup;


import java.util.ArrayList;

/**
 * 读取所有开门时段_返回结果
 */
public class ReadTimeGroup_Result implements INCommandResult {
    /**
     *返回的总数量
     */
    public int Count;

    public ArrayList<WeekTimeGroup>  ListWeekTimeGroup;

    /**
     * 初始化对象
     */
    public ReadTimeGroup_Result()
    {
        ListWeekTimeGroup = new ArrayList<WeekTimeGroup>();
    }

    /**
     * 释放资源
     */
    @Override
    public void release() {
        ListWeekTimeGroup.clear();
        ListWeekTimeGroup = null;
    }
}

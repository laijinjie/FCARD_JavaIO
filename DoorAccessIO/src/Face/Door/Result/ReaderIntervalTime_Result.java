package Face.Door.Result;

import Door.Access.Command.INCommandResult;

/**
 * 重复验证权限间隔——返回结果
 */
public class ReaderIntervalTime_Result  implements INCommandResult  {
    /**
     *读卡间隔时间，最大65535秒，0表示无限制
     */
    public int IntervalTime;
    /**
     *是否有效
     */
    public boolean IsUse;
    /**
     *检测模式<br>
     * 1 - 记录读卡，不开门，有提示<br>
     * 2 - 不记录读卡，不开门，有提示<br>
     * 3 - 不做响应，无提示<br>
     */
    public byte Mode;
    @Override
    public void release() {

    }
}

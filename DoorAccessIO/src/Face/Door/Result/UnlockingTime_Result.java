package Face.Door.Result;

import Door.Access.Command.INCommandResult;

/**
 * 开锁时输出时长
 */
public class UnlockingTime_Result implements INCommandResult {

    /**
     * 保持时间
     */
    public int ReleaseTime;

    @Override
    public void release() {

    }
}

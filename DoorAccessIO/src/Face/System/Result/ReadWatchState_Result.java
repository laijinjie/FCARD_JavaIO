package Face.System.Result;

import Door.Access.Command.INCommandResult;

public class ReadWatchState_Result implements INCommandResult {
    /**
     * 实时监控状态（0 - 未开启监控、1 - 已开启监控）
     */
    public byte WatchState;

    @Override
    public void release() {

    }
}

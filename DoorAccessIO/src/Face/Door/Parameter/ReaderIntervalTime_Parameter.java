package Face.Door.Parameter;

import Door.Access.Command.CommandDetail;
import Door.Access.Command.CommandParameter;

/**
 * 重复验证权限间隔——参数
 *
 * @author F
 */
public class ReaderIntervalTime_Parameter extends CommandParameter {
    /**
     * 读卡间隔时间，最大65535秒，0表示无限制
     */
    public int IntervalTime;
    /**
     * 是否启用
     */
    public boolean IsUse;
    /**
     * 检测模式<br>
     * 1 - 记录读卡，不开门，有提示<br>
     * 2 - 不记录读卡，不开门，有提示<br>
     * 3 - 不做响应，无提示<br>
     */
    public byte Mode;
    final int MAX_MODE = 3;
    final int MIN_MODE = 1;
    final int MAX_INTERVALTIME = 65535;
    final int MIN_INTERVALTIME = 0;

    /**
     * 构造方法
     *
     * @param detail       命令详情
     * @param isUse        是否启用
     * @param mode         检测模式
     * @param intervalTime 读卡间隔时间
     */
    public ReaderIntervalTime_Parameter(CommandDetail detail, boolean isUse, byte mode, int intervalTime) {
        super(detail);
        IsUse = isUse;
        Mode = mode;
        IntervalTime = intervalTime;
        checkParameter();
    }

    private void checkParameter() {
        if (Mode < MIN_MODE || Mode > MAX_MODE) {
            throw new UnsupportedOperationException("Mode must between 1 and 5!");
        }
        if (IntervalTime > MAX_INTERVALTIME || IntervalTime < MIN_INTERVALTIME) {
            throw new UnsupportedOperationException("IntervalTime must between 0 and 65535!");
        }
    }
}

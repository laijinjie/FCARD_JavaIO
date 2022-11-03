package Face.Door.Parameter;

import Door.Access.Command.CommandDetail;
import Door.Access.Command.CommandParameter;

/**
 * 开锁时输出时长_参数
 */
public class UnlockingTime_Parameter extends CommandParameter {
    /**
     * 输出时长
     */
    public int ReleaseTime;
    int MAX_VALUE = 65535;
    int MIN_VALUE = 0;
/**
 * 开锁时输出时长_参数
 * @param detail 命令详情
 * @param releaseTime 输出时长
 */
    public UnlockingTime_Parameter(CommandDetail detail, int releaseTime) {
        super(detail);
        ReleaseTime = releaseTime;
        checkedParameter();
    }

    private boolean checkedParameter() {
        if (ReleaseTime < MIN_VALUE || ReleaseTime > MAX_VALUE) {
            throw new UnsupportedOperationException(String.format("releaseTime must between %s and %s!", MIN_VALUE, MAX_VALUE));
        }
        return true;
    }
}

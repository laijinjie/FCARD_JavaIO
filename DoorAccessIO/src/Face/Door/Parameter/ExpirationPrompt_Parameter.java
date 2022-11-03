package Face.Door.Parameter;

import Door.Access.Command.CommandDetail;
import Door.Access.Command.CommandParameter;


/**
 * 权限到期提示参数——参数
 *
 * @author F
 */
public class ExpirationPrompt_Parameter extends CommandParameter {
    /**
     * 是否启用
     */
    public boolean IsUse;
    /**
     * 有效期阀值[1-255]
     */
    public byte Time;
    int MAX_TIME = 255;
    int MIN_TIME = 1;

    /**
     *
     * @param detail 命令详细
     * @param isUse 是否启用
     * @param time 有效期阀值
     */
    public ExpirationPrompt_Parameter(CommandDetail detail,boolean isUse,byte time) {
        super(detail);
        IsUse=isUse;
        Time=time;
        checkParameter();
    }

    private void checkParameter() {
        if (Time > MAX_TIME || Time < MIN_TIME) {
            throw new UnsupportedOperationException("Time must between 1 and 255!");
        }
    }
}

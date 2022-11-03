package Face.Door.Parameter;

import Door.Access.Command.CommandDetail;
import Door.Access.Command.CommandParameter;
/**
 * 验证码
 * @author F
 */
public class RelayOption_Parameter extends CommandParameter {
    /**
     * 继电器是否支持双稳状态
     */
    public boolean IsSupport ;
    /**
     * 验证码
     * @param detail
     * @param isSupport 是否支持
     */
    public RelayOption_Parameter(CommandDetail detail,boolean isSupport) {
        super(detail);
        IsSupport=isSupport;
    }
}

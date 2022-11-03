package Face.Door.Result;

import Door.Access.Command.INCommandResult;
/**
 * 继电器返回结果
 * @author F
 */
public class RelayOption_Result implements INCommandResult {
    /**
     * 继电器是否支持双稳状态
     */
    public boolean IsSupport ;
    @Override
    public void release() {

    }
}

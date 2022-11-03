package Face.Door.Result;

import Door.Access.Command.INCommandResult;

/**
 * 权限到期提示参数——返回值
 * @author F
 */
public class ExpirationPrompt_Result implements INCommandResult {
    /**
     *是否启用
     */
    public  boolean IsUse;
    /**
     *有效期阀值[1-255]
     */
    public byte Time;

    @Override
    public void release() {

    }
}

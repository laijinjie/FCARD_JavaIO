package Face.Door.Result;

import Door.Access.Command.INCommandResult;

/**
 * 免验证开门_返回结果
 * @author F
 */
public class ExemptionVerificationOpen_Result implements INCommandResult {
    /**
     * 启用免验证
     */
    public boolean IsUseExemptionVerification;
    /**
     * 启用自动注册
     */
    public boolean IsUseAutomaticRegistration;
    /**
     * 自动注册时段编号
     */
    public byte PeriodNumber;
    @Override
    public void release() {

    }
}

package Face.Door.Parameter;

import Door.Access.Command.CommandDetail;
import Door.Access.Command.CommandParameter;

/**
 * 免验证开门_读取_参数
 *
 * @author F
 */
public class ExemptionVerificationOpen_Parameter extends CommandParameter {

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

    /**
     * 免验证开门_读取_参数
     *
     * @param detail 命令详情
     * @param isUseExemptionVerification 启用免验证
     * @param isUseAutomaticRegistration 启用自动注册
     * @param periodNumber 自动注册时段编号
     */
    public ExemptionVerificationOpen_Parameter(CommandDetail detail, boolean isUseExemptionVerification, boolean isUseAutomaticRegistration, byte periodNumber) {
        super(detail);
        IsUseExemptionVerification = isUseExemptionVerification;
        IsUseAutomaticRegistration = isUseAutomaticRegistration;
        PeriodNumber = periodNumber;
    }
}

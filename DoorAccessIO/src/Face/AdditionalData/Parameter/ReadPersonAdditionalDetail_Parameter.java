package Face.AdditionalData.Parameter;

import Door.Access.Command.CommandDetail;
import Door.Access.Command.CommandParameter;

/**
 * 查询人员附加数据详情_查询条件
 */
public class ReadPersonAdditionalDetail_Parameter extends CommandParameter {
    /**
     * 用户号
     */
    public int UserCode;
    /**
     * 查询人员附加数据详情
     * @param detail 命令详情
     * @param userCode 用户号
     */
    public ReadPersonAdditionalDetail_Parameter(CommandDetail detail, int userCode) {
        super(detail);
        UserCode=userCode;
    }
}

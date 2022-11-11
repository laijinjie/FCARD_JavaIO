package Face.Person.Parameter;

import Door.Access.Command.CommandDetail;
import Door.Access.Command.CommandParameter;

/**
 * 查询人员数据详情
 * @author F
 */
public class ReadPersonDetail_Parameter extends CommandParameter {
    /**
     * 用户号
     */
    public long UserCode;
    /**
     * 查询人员数据详情
     * @param detail 命令详情
     * @param userCode 用户号
     */
    public ReadPersonDetail_Parameter(CommandDetail detail,long userCode) {
        super(detail);
        UserCode=userCode;
    }
}

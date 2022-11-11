package Face.Person.Parameter;

import Door.Access.Command.CommandDetail;
import Door.Access.Command.CommandParameter;

import java.util.ArrayList;


/**
 * 删除人员信息
 * @author F
 */
public class DeletePerson_Parameter extends CommandParameter {
    /**
     * 用户号列表
     */
   public ArrayList<Long> UserCodeList;

    /**
     * 删除人员信息参数
     * @param detail 命令详情
     * @param userCodeList 待删除人员编号列表
     */
    public DeletePerson_Parameter(CommandDetail detail, ArrayList<Long> userCodeList ) {
        super(detail);
        UserCodeList=userCodeList;
    }
}

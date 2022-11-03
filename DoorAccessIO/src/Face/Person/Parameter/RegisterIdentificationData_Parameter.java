package Face.Person.Parameter;

import Door.Access.Command.CommandDetail;
import Door.Access.Command.CommandParameter;
import Face.Data.Person;

/**
 * 注册人员识别信息参数
 * @author F
 */
public class RegisterIdentificationData_Parameter extends CommandParameter {
    /**
     * 人员详情
     */
    public Person PersonDetail;
    /**
     * 注册类型
     */
    public int DataType;
    /**
     * 序号
     */
    public int DataNum;
    /**
     * 注册人员识别信息参数
     * @param detail 命令详情
     * @param personDetail 人员详情
     * @param dataType 注册类型
     */
    public RegisterIdentificationData_Parameter(CommandDetail detail,Person personDetail,int dataType) {
        super(detail);
        PersonDetail=personDetail;
        DataType=dataType;
        if (DataType != 1) {
            DataNum = 1;
        }
    }
}

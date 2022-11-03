package Face.Person.Result;

import Door.Access.Command.INCommandResult;
import Face.Data.IdentificationData;
import Face.Data.Person;

/**
 * 注册人员识别信息返回值
 *
 * @author F
 */
public class RegisterIdentificationData_Result implements INCommandResult {

    /**
     * 注册 指纹、红外人脸、动态人脸返回值
     */
    public IdentificationData ResultData;
    /**
     * 状态代码<br>
     * 1、已开始注册<br>
     * 2、用户号不存在<br>
     * 3、类型错误或不支持<br>
     * 4、序号已超出范围<br>
     * 5、设备存储空间已满<br>
     * 101、注册成功<br>
     * 102、用户取消操作
     */
    public int Status;
    /**
     * 注册刷卡、密码 返回值
     */
    public Person PersonDetail;
    /**
     * 数据类型,取值范围：1-4 1、人员照片 2、指纹特征码 3、红外人脸特征码 4、动态人脸特征码
     */
    public int DataType;
    /**
     * 数据序号 数据类型为2[指纹特征码]时，取值范围：0-9
     */
    public int DataNum;

    /**
     * 注册人员识别信息返回值
     *
     * @param personDetail 人员信息详情
     * @param dataType 注册类型
     * @param dataNum 序号
     */
    public RegisterIdentificationData_Result(Person personDetail, int dataType, int dataNum) {
        PersonDetail = personDetail;
        DataType = dataType;
        DataNum = dataNum;
    }

    @Override
    public void release() {

    }
}

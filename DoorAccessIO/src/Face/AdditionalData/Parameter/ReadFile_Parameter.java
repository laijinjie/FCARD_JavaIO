package Face.AdditionalData.Parameter;

import Door.Access.Command.CommandDetail;

/**
 * 读取大型文件的参数
 *
 * @author F
 */
public class ReadFile_Parameter extends ReadFeatureCode_Parameter {

    /**
     * 读取人员照片/记录照片/指纹
     *
     * @param detail 通讯参数
     * @param userCode 用户号/当读取认证图片时为记录序号
     * @param type 读取类型
     * @param serialNumber 序号
     */
    public ReadFile_Parameter(CommandDetail detail, long userCode, int type, int serialNumber) {
        super(detail, userCode, type, serialNumber);
    }

}

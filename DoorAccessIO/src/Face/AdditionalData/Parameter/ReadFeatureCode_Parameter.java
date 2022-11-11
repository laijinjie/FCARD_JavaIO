package Face.AdditionalData.Parameter;

import Door.Access.Command.CommandDetail;
import Door.Access.Command.CommandParameter;
import io.netty.buffer.ByteBuf;

/**
 * 读取人员照片/记录照片/指纹
 *
 * @author F
 */
public class ReadFeatureCode_Parameter extends CommandParameter {

    /**
     * 用户号
     */
    public long UserCode;

    /**
     * 文件类型 1 - 人员头像； 2 - 指纹； 3 - 记录照片； 4 - 红外人脸特征码； 5 - 动态人脸特征码；
     */
    public int Type;

    /**
     * 序号
     */
    public int SerialNumber;

    /**
     * 读取人员照片/记录照片/指纹
     *
     * @param detail 通讯参数
     * @param userCode 用户号
     * @param type 1 - 人员头像； 2 - 指纹； 3 - 记录照片； 4 - 红外人脸特征码； 5 - 动态人脸特征码；
     * @param serialNumber 序号
     */
    public ReadFeatureCode_Parameter(CommandDetail detail, long userCode, int type, int serialNumber) {
        super(detail);
        UserCode = userCode;
        Type = type;
        SerialNumber = serialNumber;
    }

    /**
     * 内部使用
     *
     * @param buf
     */
    public void getBytes(ByteBuf buf) {
        buf.writeByte(Type);
        buf.writeByte(SerialNumber);
        buf.writeInt((int) UserCode);
    }
}

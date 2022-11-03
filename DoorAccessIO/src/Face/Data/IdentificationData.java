package Face.Data;

/**
 * 人员识别信息
 *
 * @author F
 */
public class IdentificationData {
    /**
     * 数据类型,取值范围：1-4
     * 1、人员照片
     * 2、指纹特征码
     * 3、红外人脸特征码
     * 4、动态人脸特征码
     */
    public int DataType;
    /**
     * 数据序号
     * 数据类型为2[指纹特征码]时，取值范围：0-9
     */
    public int DataNum;
    /**
     * 数据缓冲区
     */
    public byte[] DataBuf;

    /**
     * 创建 人员识别信息
     * @param iType 数据类型
     * @param bData 数据缓存区
     */
    public IdentificationData(int iType, byte[] bData) {
        this(iType, 1, bData);
    }

    /**
     * 创建 人员识别信息
     * @param iType 数据类型
     * @param iNum 数据序号
     * @param bData 数据缓存区
     */
    public IdentificationData(int iType, int iNum, byte[] bData) {
        DataType = iType;
        DataNum = iNum;
        DataBuf = bData;
    }
}

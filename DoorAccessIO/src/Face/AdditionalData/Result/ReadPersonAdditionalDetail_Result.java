package Face.AdditionalData.Result;

import Door.Access.Command.INCommandResult;
import io.netty.buffer.ByteBuf;

public class ReadPersonAdditionalDetail_Result implements INCommandResult {
    /**
     * 用户号
     */
    public int UserCode;

    /**
     * 人员头像列表 1表示有，0表示无
     * 照片1-5
     */
    public byte[] PhotoList;

    /**
     * 指纹列表 1表示有，0表示无
     * 指纹1-10
     */
    public byte[] FingerprintList;

    /**
     * 人脸特征码
     */
    public boolean HasFace;
    @Override
    public void release() {

    }

    /**
     * 数据包赋值
     * @param buf 数据包
     */
    public void setBytes(ByteBuf buf)
    {
        UserCode = buf.readInt();
        PhotoList = new byte[5];
        buf.readBytes(PhotoList, 0, 5);

        FingerprintList = new byte[10];
        buf.readBytes(FingerprintList, 0, 10);
        HasFace = buf.readBoolean();
    }
}

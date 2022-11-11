package Face.AdditionalData.Result;

import Door.Access.Command.INCommandResult;
import io.netty.buffer.ByteBuf;

/**
 * 读取人员照片/记录照片/指纹 返回结果
 *
 * @author F
 */
public class ReadFeatureCode_Result implements INCommandResult {

    /**
     * 用户号
     */
    public long UserCode;

    /**
     * 文件类型 1 - 人员头像 2 - 指纹 3 - 记录照片 4 - 红外人脸特征码 5 - 动态人脸特征码
     */
    public int FileType;
    /**
     * 文件句柄
     */
    public long FileHandle;

    /**
     * 文件大小
     */
    public int FileSize;
    /**
     * 数据
     */
    public byte[] FileDatas;

    /**
     * CRC32校验数据
     */
    public long FileCRC;

    /**
     * 指示命令执行结果
     */
    public boolean Result;

    @Override
    public void release() {

    }

    /**
     * 数据转换（内部使用）
     *
     * @param buf
     */
    public void setBytes(ByteBuf buf) {
        FileType = buf.readByte();
        UserCode = buf.readUnsignedInt();
        FileHandle = buf.readUnsignedInt();
        FileSize = buf.readMedium();
    }

}

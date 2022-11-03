package Face.AdditionalData.Result;

import io.netty.buffer.ByteBuf;

/**
 * 读取文件块返回结果
 * @author F
 */
public class ReadFile_Result extends ReadFeatureCode_Result {
    /**
     * 数据转换（内部使用）
     * @param buf 
     */
    @Override
    public void setBytes(ByteBuf buf) {
        FileType = buf.readByte();
        UserCode = buf.readInt();
        FileHandle = buf.readInt();
        FileSize = buf.readInt();
        if (FileSize < 0)
        {
            FileHandle = 0;
            FileSize = 0;
        }
        FileCRC =  buf.readInt();
    }
}

package Face.System.Result;

import Door.Access.Command.INCommandResult;
import Door.Access.Util.ByteUtil;
import io.netty.buffer.ByteBuf;

/**
 *  返回值韦根输出
 */
public class ReadWiegandOutput_Result implements INCommandResult {

    /**
     * 读卡字节
     * 1 - 韦根26(三字节)
     * 2 - 韦根34(四字节)
     * 3 - 韦根26(二字节)
     * 4 - 韦根66(八字节)
     * 5 - 禁用
     */
    public  byte ReadCardByte;
    /**
     * WG输出功能开关
     * 1 - 启用
     * 2 - 禁用
     */
    public  byte WGOutputSwitch;
    /**
     * WG字节顺序
     * 1 - 高位在前低位在后
     * 2 - 低位在前高位在后
     */
    public byte WGByteSort;
    /**
     * 输出数据类型
     * 1 - 输出用户号
     * 2 - 输出人员卡号
     */
    public byte OutputType;



    /**
     * 初始化实例
     */
    public ReadWiegandOutput_Result(){

    }



    public  int GetDataLen(){
        return 0x04;
    }



    /**
     *设置参数，内部使用
     * @param buf
     */
    public  void SetBytes(ByteBuf buf)
    {
        ReadCardByte = buf.readByte();
        WGOutputSwitch = buf.readByte();
        WGByteSort = buf.readByte();
        OutputType = buf.readByte();
    }

    @Override
    public void release() {

    }
}

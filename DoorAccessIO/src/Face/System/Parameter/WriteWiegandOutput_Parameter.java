package Face.System.Parameter;

import Door.Access.Command.CommandDetail;
import Door.Access.Command.CommandParameter;
import Door.Access.Util.ByteUtil;
import io.netty.buffer.ByteBuf;

/**
 * 设置韦根
 */
public class WriteWiegandOutput_Parameter extends CommandParameter {
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
     * @param readCardByte 读卡字节
     * @param wGOutputSwitch WG输出功能开关
     * @param wGByteSort WG字节顺序
     * @param outputType 输出数据类型
     */
    public WriteWiegandOutput_Parameter(CommandDetail detail,byte readCardByte , byte wGOutputSwitch, byte wGByteSort, byte outputType)
    {
        super(detail);
        ReadCardByte = readCardByte;
        WGOutputSwitch = wGOutputSwitch;
        WGByteSort = wGByteSort;
        OutputType = outputType;
    }
     /**
     *返回数据包，内部使用
     * @return
     */
    public ByteBuf GetBytes(){
        ByteBuf buf= ByteUtil.ALLOCATOR.buffer(GetDataLen());
        buf.writeByte(ReadCardByte);
        buf.writeByte(WGOutputSwitch);
        buf.writeByte(WGByteSort);
        buf.writeByte(OutputType);
        return buf;
    }

    public  int GetDataLen(){
        return 0x04;
    }
    public  boolean checkedParameter()
    {
        if (ReadCardByte > 5 || ReadCardByte < 1)
        {
            return false;
        }
        if (WGOutputSwitch > 2 || ReadCardByte < 1)
        {
            return false;
        }
        if (WGByteSort > 2 || WGByteSort < 1)
        {
            return false;
        }
        if (OutputType > 2 || OutputType < 1)
        {
            return false;
        }
        return true;
    }
}

package Face.Door;

import Door.Access.Door8800.Command.Door8800Command;
import Door.Access.Util.ByteUtil;
import Face.Door.Parameter.ReaderOption_Parameter;
import io.netty.buffer.ByteBuf;

/**
 * 读卡器字节数_写入
 */
public class WriteReaderOption extends Door8800Command {

    /**
     * 读卡器字节数_写入
     * @param par 读卡器字节数_写入参数
     */
    public WriteReaderOption(ReaderOption_Parameter par) {
        _Parameter = par;
        CreatePacket(0x03, 0x01, 0x01, 0x01, GetBuf(par));
    }

    private ByteBuf GetBuf(ReaderOption_Parameter par) {
            ByteBuf buf= ByteUtil.ALLOCATOR.buffer(1);
            buf.writeByte(par.ReaderOption);
            return  buf;
    }

    @Override
    protected void Release0() {
    }
}

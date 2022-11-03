package Face.Door;

import Door.Access.Door8800.Command.Door8800Command;
import Door.Access.Util.ByteUtil;
import Face.Door.Parameter.RelayOption_Parameter;
import io.netty.buffer.ByteBuf;

/**
 *继电器参数_写入
 */
public class WriteRelayOption extends Door8800Command {

    int DATA_LEN = 0x01;
/**
 * 继电器参数_写入
 * @param par 继电器参数_写入参数
 */
    public WriteRelayOption(RelayOption_Parameter par) {
        _Parameter = par;
        CreatePacket(0x03, 0x02, 0x01, DATA_LEN, getDataBuf(par));
    }
    @Override
    protected void Release0() {

    }

    private ByteBuf getDataBuf(RelayOption_Parameter par) {
        ByteBuf buf = ByteUtil.ALLOCATOR.buffer(DATA_LEN);
        buf.writeBoolean(par.IsSupport);
        return buf;
    }
}

package Face.Door;

import Door.Access.Door8800.Command.Door8800Command;
import Door.Access.Util.ByteUtil;
import Face.Door.Parameter.UnlockingTime_Parameter;
import io.netty.buffer.ByteBuf;

/**
 * 开锁时输出时长
 */
public class WriteUnlockingTime extends Door8800Command {

    int DATA_LEN=0x02;
    /**
     * 开锁时输出时长
     * @param par 开锁时输出时长参数
     */
    public  WriteUnlockingTime(UnlockingTime_Parameter par){
        _Parameter=par;
        CreatePacket(0x03,0x08,0x01,DATA_LEN,getDataBuf(par));
    }

    private ByteBuf getDataBuf(UnlockingTime_Parameter par){
        ByteBuf buf= ByteUtil.ALLOCATOR.buffer(DATA_LEN);
        buf.writeShort(par.ReleaseTime);
        return  buf;
    }
    @Override
    protected void Release0() {

    }
}

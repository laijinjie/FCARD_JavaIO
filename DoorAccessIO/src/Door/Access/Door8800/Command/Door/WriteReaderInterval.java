/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Door.Access.Door8800.Command.Door;

import Door.Access.Door8800.Command.Door.Parameter.WriteReaderInterval_Parameter;
import Door.Access.Door8800.Command.Door8800Command;
import Door.Access.Util.ByteUtil;
import io.netty.buffer.ByteBuf;

/**
 * 重复读卡间隔<br>
 * 间隔时间，需要调用函数 {@link Door.Access.Door8800.Command.System.WriteReaderIntervalTime}
 * @author 赖金杰
 */
public class WriteReaderInterval extends Door8800Command {

    public WriteReaderInterval(WriteReaderInterval_Parameter par) {
        _Parameter = par;
        ByteBuf dataBuf = ByteUtil.ALLOCATOR.buffer(3);
        dataBuf.writeByte(par.DoorNum);
        dataBuf.writeBoolean(par.Use);
        dataBuf.writeByte(par.RecordOption);
        CreatePacket(3, 9, 1, 3, dataBuf);
    }

    @Override
    protected void Release0() {
    }

}

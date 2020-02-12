/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Door.Access.Door8800.Command.Door;

import Door.Access.Door8800.Command.Door.Parameter.WriteRelayReleaseTime_Parameter;
import Door.Access.Door8800.Command.Door8800Command;
import Door.Access.Util.ByteUtil;
import io.netty.buffer.ByteBuf;

/**
 * 开门保持时间<br/>
 * 继电器开锁后释放时间<br/>
 *
 * @author 赖金杰
 */
public class WriteRelayReleaseTime extends Door8800Command {

    public WriteRelayReleaseTime(WriteRelayReleaseTime_Parameter par) {
        _Parameter = par;
        ByteBuf dataBuf = ByteUtil.ALLOCATOR.buffer(3);
        dataBuf.writeByte(par.Door);
        dataBuf.writeShort(par.ReleaseTime);
        CreatePacket(3, 8, 1, 3, dataBuf);
    }

    @Override
    protected void Release0() {
    }

}

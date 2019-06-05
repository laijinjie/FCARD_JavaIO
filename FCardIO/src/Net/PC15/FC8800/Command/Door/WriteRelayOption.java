/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Net.PC15.FC8800.Command.Door;

import Net.PC15.FC8800.Command.Door.Parameter.WriteReaderOption_Parameter;
import Net.PC15.FC8800.Command.FC8800Command;
import Net.PC15.Util.ByteUtil;
import io.netty.buffer.ByteBuf;

/**
 * 控制器继电器参数<br/>
 *
 * @author 赖金杰
 */
public class WriteRelayOption extends FC8800Command {

    public WriteRelayOption(WriteReaderOption_Parameter par) {
        _Parameter = par;
        ByteBuf dataBuf = ByteUtil.ALLOCATOR.buffer(4);
        dataBuf.writeBytes(par.door.DoorPort);
        CreatePacket(3, 2, 1, 4, dataBuf);
    }

    @Override
    protected void Release0() {
    }

}

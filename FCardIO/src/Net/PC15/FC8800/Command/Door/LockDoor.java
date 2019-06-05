/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Net.PC15.FC8800.Command.Door;

import Net.PC15.FC8800.Command.Door.Parameter.RemoteDoor_Parameter;
import Net.PC15.FC8800.Command.FC8800Command;
import Net.PC15.Util.ByteUtil;
import io.netty.buffer.ByteBuf;

/**
 * 远程锁定门，锁定后不能读卡开门，仅可以密码开门，不能按钮开门<br/>
 * 每个门的值0表示不执行操作，1表示执行操作
 *
 * @author 赖金杰
 */
public class LockDoor extends FC8800Command {

    public LockDoor(RemoteDoor_Parameter par) {
        _Parameter = par;
        ByteBuf dataBuf = ByteUtil.ALLOCATOR.buffer(4);
        dataBuf.writeBytes(par.Door.DoorPort);
        CreatePacket(3, 4, 0, 4, dataBuf);
    }

    @Override
    protected void Release0() {
    }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Net.PC15.FC8800.Command.System;

import Net.PC15.FC8800.Command.FC8800Command;
import Net.PC15.FC8800.Command.System.Parameter.WriteKeepAliveInterval_Parameter;
import Net.PC15.Util.ByteUtil;
import io.netty.buffer.ByteBuf;

/**
 * 设置控制器作为客户端时，和服务器的保活间隔时间
 *
 * @author 赖金杰
 */
public class WriteKeepAliveInterval extends FC8800Command {

    public WriteKeepAliveInterval(WriteKeepAliveInterval_Parameter par) {
        _Parameter = par;
        ByteBuf dataBuf = ByteUtil.ALLOCATOR.buffer(2);
        dataBuf.writeShort(par.IntervalTime);
        CreatePacket(1, 0xF0, 2, 2, dataBuf);
    }

    @Override
    protected void Release0() {
        return;
    }

}

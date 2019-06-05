/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Net.PC15.FC8800.Command.System;

import Net.PC15.FC8800.Command.FC8800Command;
import Net.PC15.FC8800.Command.System.Parameter.WriteReaderIntervalTime_Parameter;
import Net.PC15.Util.ByteUtil;
import io.netty.buffer.ByteBuf;

/**
 * 读卡间隔时间参数<br/>
 * 门是否启用读卡间隔，需要调用函数 {@link Net.PC15.FC8800.Command.Door.WriteReaderInterval}
 * @author 赖金杰
 */
public class WriteReaderIntervalTime extends FC8800Command {

    public WriteReaderIntervalTime(WriteReaderIntervalTime_Parameter par) {
        _Parameter = par;

        ByteBuf dataBuf = ByteUtil.ALLOCATOR.buffer(2);
        dataBuf.writeShort(par.IntervalTime);
        CreatePacket(1, 0xA, 7, 2, dataBuf);
    }

    @Override
    protected void Release0() {
        return;
    }

}

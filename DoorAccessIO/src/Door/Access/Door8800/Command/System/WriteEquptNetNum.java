/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Door.Access.Door8800.Command.System;

import Door.Access.Door8800.Command.Door8800Command;
import Door.Access.Door8800.Command.System.Parameter.SearchEquptOnNetNum_Parameter;
import Door.Access.Util.ByteUtil;
import io.netty.buffer.ByteBuf;

/**
 * 根据SN设置网络标记
 *
 * @author 赖金杰
 */
public class WriteEquptNetNum extends Door8800Command {

    public WriteEquptNetNum(SearchEquptOnNetNum_Parameter par) {
        _Parameter = par;
        ByteBuf dataBuf = ByteUtil.ALLOCATOR.buffer(2);
        dataBuf.writeShort(par.NetNum);
        CreatePacket(1, 0xFE, 1, 2, dataBuf);
    }

    @Override
    protected void Release0() {
        return;
    }

}

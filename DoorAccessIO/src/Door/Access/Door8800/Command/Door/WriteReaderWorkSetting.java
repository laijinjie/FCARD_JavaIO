/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Door.Access.Door8800.Command.Door;

import Door.Access.Door8800.Command.Door.Parameter.WriteReaderWorkSetting_Parameter;
import Door.Access.Door8800.Command.Door8800Command;
import Door.Access.Util.ByteUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;

/**
 * 设置控制器单个门的读卡器验证方式参数<br>
 * 读卡器验证方式是按周时段划分，验证方式分为：只读卡\只密码\读卡加密码\手动输入卡号+密码
 *
 * @author 赖金杰
 */
public class    WriteReaderWorkSetting extends Door8800Command {

    public WriteReaderWorkSetting(WriteReaderWorkSetting_Parameter par) {
        _Parameter = par;
        ByteBuf dataBuf = ByteUtil.ALLOCATOR.buffer(0x119);
        dataBuf.writeByte(par.DoorNum);
        par.ReaderWork.GetBytes(dataBuf);
     //  System.out.println(ByteBufUtil.hexDump(dataBuf));
        CreatePacket(3, 5, 1, 0x119, dataBuf);
    }

    @Override
    protected void Release0() {
    }

}

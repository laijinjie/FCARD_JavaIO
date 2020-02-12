/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Door.Access.Door8800.Command.DateTime;

import Door.Access.Door8800.Command.DateTime.Parameter.WriteTimeDefine_Parameter;
import Door.Access.Door8800.Command.Door8800Command;
import Door.Access.Util.ByteUtil;
import Door.Access.Util.TimeUtil;
import io.netty.buffer.ByteBuf;
import java.util.Calendar;

/**
 * 写入自定义时间到控制器
 *
 * @author 赖金杰
 */
    public class WriteTimeDefine extends Door8800Command {

    public WriteTimeDefine(WriteTimeDefine_Parameter par) {
        _Parameter = par;
        byte Datebuf[] = new byte[7];
        TimeUtil.DateToBCD_ssmmhhddMMwwyy(Datebuf, par.ControllerDate);

        ByteBuf dataBuf = ByteUtil.ALLOCATOR.buffer(7);
        dataBuf.writeBytes(Datebuf);

        CreatePacket(2, 2, 0, 7, dataBuf);
    }

    @Override
    protected void Release0() {
        return;
    }

}

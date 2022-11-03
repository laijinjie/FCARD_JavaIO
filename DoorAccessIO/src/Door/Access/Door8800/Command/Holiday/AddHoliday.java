/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Door.Access.Door8800.Command.Holiday;

import Door.Access.Door8800.Command.Door8800Command;
import Door.Access.Door8800.Command.Holiday.Parameter.AddHoliday_Parameter;
import Door.Access.Util.ByteUtil;
import io.netty.buffer.ByteBuf;

/**
 *
 * @author FCARD
 */
public class AddHoliday extends Door8800Command {

    public AddHoliday(AddHoliday_Parameter parameter) {
        _Parameter = parameter;
        ByteBuf buf = parameter.GetBytes();
        CreatePacket(0x04, 0x04, 0x00, buf.readableBytes(), buf);
    }

    @Override
    protected void Release0() {

    }

}

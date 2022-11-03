/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Door.Access.Door8800.Command.Holiday;

import Door.Access.Command.CommandParameter;
import Door.Access.Connector.INConnectorEvent;
import Door.Access.Door8800.Command.Door8800Command;
import Door.Access.Door8800.Command.Holiday.Result.ReadAllHoliday_Result;
import Door.Access.Door8800.Command.Holiday.Result.ReadHolidayDetail_Result;
import Door.Access.Door8800.Packet.Door8800PacketModel;
import io.netty.buffer.ByteBuf;

/**
 * 已存储的所有节假日
 *
 * @author FCARD
 */
public class ReadAllHoliday extends Door8800Command {

    public ReadAllHoliday(CommandParameter par) {
        _Parameter = par;
        CreatePacket(0x04, 3);
        _Result = new ReadAllHoliday_Result();
    }

    @Override
    protected void Release0() {

    }

    @Override
    protected boolean _CommandStep(INConnectorEvent oEvent, Door8800PacketModel model) {
        if (CheckResponse_Cmd(model, 0x04, 0x03, 0x00)) {
            ByteBuf buf = model.GetDatabuff();
            ReadAllHoliday_Result result = (ReadAllHoliday_Result) _Result;
            result.SetBytes(buf);
            CommandWaitResponse();
        }
        if (CheckResponse_Cmd(model, 0x04, 0x03, 0xFF)) {
            RaiseCommandCompleteEvent(oEvent);
        }
        return true;
    }

}

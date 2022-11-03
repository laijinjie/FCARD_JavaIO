/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Door.Access.Door8800.Command.Holiday;

import Door.Access.Command.CommandParameter;
import Door.Access.Connector.INConnectorEvent;
import Door.Access.Door8800.Command.Door8800Command;
import Door.Access.Door8800.Command.Holiday.Result.ReadHolidayDetail_Result;
import Door.Access.Door8800.Packet.Door8800PacketModel;
import io.netty.buffer.ByteBuf;

/**
 * 读取节假日存储详情
 *
 * @author FCARD
 */
public class ReadHolidayDetail extends Door8800Command {

    /**
     * 读取节假日存储详情
     *
     * @param par 命令参数
     */
    public ReadHolidayDetail(CommandParameter par) {
        _Parameter = par;
        CreatePacket(0x04, 0x01);
    }

    @Override
    protected void Release0() {

    }

    @Override
    protected boolean _CommandStep(INConnectorEvent oEvent, Door8800PacketModel model) {
        if (CheckResponse_Cmd(model, 0x04, 1, 0)) {
            ByteBuf buf = model.GetDatabuff();
            ReadHolidayDetail_Result result = new ReadHolidayDetail_Result();
            result.SetBytes(buf);
            _Result = result;
            RaiseCommandCompleteEvent(oEvent);
            return true;
        }
        return false;
    }
}

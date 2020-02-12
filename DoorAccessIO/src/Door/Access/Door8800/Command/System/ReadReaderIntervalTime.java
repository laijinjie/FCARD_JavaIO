/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Door.Access.Door8800.Command.System;

import Door.Access.Command.CommandParameter;
import Door.Access.Connector.INConnectorEvent;
import Door.Access.Door8800.Command.Door8800Command;
import Door.Access.Door8800.Command.System.Result.ReadReaderIntervalTime_Result;
import Door.Access.Door8800.Packet.Door8800PacketModel;
import io.netty.buffer.ByteBuf;

/**
 * 读卡间隔时间参数.<br/>
 * 成功返回结果参考 {@link ReadReaderIntervalTime_Result}<br/>
 * 门的读卡间隔功能启用，需要调用函数 {@link Door.Access.Door8800.Command.Door.ReadReaderInterval}
 *
 * @author 赖金杰
 */
public class ReadReaderIntervalTime extends Door8800Command {

    public ReadReaderIntervalTime(CommandParameter par) {
        _Parameter = par;
        CreatePacket(1, 0xA, 0x87);
    }

    @Override
    protected void Release0() {
        return;
    }

    @Override
    protected boolean _CommandStep(INConnectorEvent oEvent, Door8800PacketModel model) {
        if (CheckResponse_Cmd(model, 1, 0xA, 0x87, 2)) {
            ByteBuf buf = model.GetDatabuff();
            //设定返回值
            ReadReaderIntervalTime_Result ret = new ReadReaderIntervalTime_Result();
            _Result = ret;
            ret.IntervalTime = buf.readUnsignedShort();
            RaiseCommandCompleteEvent(oEvent);
            return true;
        } else {
            return false;
        }
    }

}

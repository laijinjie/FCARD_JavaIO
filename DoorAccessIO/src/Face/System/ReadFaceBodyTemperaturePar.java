/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Face.System;

import Door.Access.Command.CommandParameter;
import Door.Access.Connector.INConnectorEvent;
import Door.Access.Door8800.Command.Door8800Command;
import Door.Access.Door8800.Packet.Door8800PacketModel;
import Face.System.Result.ReadFaceBodyTemperaturePar_Result;
import io.netty.buffer.ByteBuf;

/**
 * 读取体温检测开关及体温格式
 *
 * @author F
 */
public class ReadFaceBodyTemperaturePar extends Door8800Command {

    public ReadFaceBodyTemperaturePar(CommandParameter parameter) {
        _Parameter = parameter;
        CreatePacket(1, 41, 1);
    }

    @Override
    protected boolean _CommandStep(INConnectorEvent oEvent, Door8800PacketModel model) {
        if (CheckResponse_Cmd(model, 2)) {
            ByteBuf buf = model.GetDatabuff();
            int bodyTemperaturePar = buf.readByte();
            if (bodyTemperaturePar != 0) {
                bodyTemperaturePar = buf.readByte();
            }
            _Result = new ReadFaceBodyTemperaturePar_Result(bodyTemperaturePar);
            RaiseCommandCompleteEvent(oEvent);
            return true;
        }
        return false;
    }

    @Override
    protected void Release0() {

    }

}

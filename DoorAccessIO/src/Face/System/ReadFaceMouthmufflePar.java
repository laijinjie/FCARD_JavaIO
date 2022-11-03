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
import Face.System.Result.ReadFaceMouthmufflePar_Result;

/**
 * 读取口罩识别开关
 *
 * @author F
 */
public class ReadFaceMouthmufflePar extends Door8800Command {

    /**
     * 读取口罩识别开关
     *
     * @param parameter
     */
    public ReadFaceMouthmufflePar(CommandParameter parameter) {
        _Parameter = parameter;
        CreatePacket(0x01, 40, 0x01);
    }

    @Override
    protected boolean _CommandStep(INConnectorEvent oEvent, Door8800PacketModel model) {
        if (CheckResponse_Cmd(model, 1)) {
            _Result = new ReadFaceMouthmufflePar_Result(model.GetDatabuff().readByte());
            RaiseCommandCompleteEvent(oEvent);
            return true;
        }
        return false;
    }

    @Override
    protected void Release0() {

    }
}

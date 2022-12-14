/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Door.Access.Door8800.Command.System;

import Door.Access.Command.CommandParameter;
import Door.Access.Connector.INConnectorEvent;
import Door.Access.Door8800.Command.Data.DoorLimit;
import Door.Access.Door8800.Command.Door8800Command;
import Door.Access.Door8800.Command.System.Result.ReadEnterDoorLimit_Result;
import Door.Access.Door8800.Packet.Door8800PacketModel;
import io.netty.buffer.ByteBuf;

/**
 * 门内人数上限参数.<br>
 * 成功返回结果参考 {@link ReadEnterDoorLimit_Result}
 *
 * @author 赖金杰
 */
public class ReadEnterDoorLimit extends Door8800Command {

    public ReadEnterDoorLimit(CommandParameter par) {
        _Parameter = par;
        CreatePacket(1, 0xA, 0x8C);
    }

    @Override
    protected void Release0() {
        return;
    }

    @Override
    protected boolean _CommandStep(INConnectorEvent oEvent, Door8800PacketModel model) {
        if (CheckResponse_Cmd(model, 1, 0xA, 0x8C, 40)) {
            ByteBuf buf = model.GetDatabuff();
            //设定返回值
            ReadEnterDoorLimit_Result ret = new ReadEnterDoorLimit_Result();
            _Result = ret;
            DoorLimit dl = new DoorLimit();
            dl.GlobalLimit = buf.readUnsignedInt();
            for (int i = 0; i < 4; i++) {
                dl.DoorLimit[i] = buf.readUnsignedInt();
            }
            dl.GlobalEnter = buf.readUnsignedInt();
            for (int i = 0; i < 4; i++) {
                dl.DoorEnter[i] = buf.readUnsignedInt();
            }
            ret.Limit = dl;
            RaiseCommandCompleteEvent(oEvent);
            return true;
        } else {
            return false;
        }
    }

}

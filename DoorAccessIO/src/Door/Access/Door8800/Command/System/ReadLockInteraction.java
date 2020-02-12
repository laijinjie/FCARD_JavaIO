/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Door.Access.Door8800.Command.System;

import Door.Access.Command.CommandParameter;
import Door.Access.Connector.INConnectorEvent;
import Door.Access.Door8800.Command.Door8800Command;
import Door.Access.Door8800.Command.System.Result.ReadLockInteraction_Result;
import Door.Access.Door8800.Packet.Door8800PacketModel;
import io.netty.buffer.ByteBuf;

/**
 * 互锁功能开关.<br/>
 * 成功返回结果参考 {@link ReadLockInteraction_Result}
 *
 * @author 赖金杰
 */
public class ReadLockInteraction extends Door8800Command {

    public ReadLockInteraction(CommandParameter par) {
        _Parameter = par;
        CreatePacket(1, 0xA, 0x84);
    }

    @Override
    protected void Release0() {
        return;
    }

    @Override
    protected boolean _CommandStep(INConnectorEvent oEvent, Door8800PacketModel model) {
        if (CheckResponse_Cmd(model, 1, 0xA, 0x84, 4)) {
            ByteBuf buf = model.GetDatabuff();
            //设定返回值
            ReadLockInteraction_Result ret = new ReadLockInteraction_Result();
            _Result = ret;

            byte btTmp[] = new byte[4];
            buf.readBytes(btTmp, 0, 4);
            for (int i = 1; i < 4; i++) {
                ret.DoorPort.SetDoor(i, btTmp[i - 1]);
            }

            RaiseCommandCompleteEvent(oEvent);
            return true;
        } else {
            return false;
        }

    }
}

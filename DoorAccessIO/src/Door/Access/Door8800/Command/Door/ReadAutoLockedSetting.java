/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Door.Access.Door8800.Command.Door;

import Door.Access.Connector.INConnectorEvent;
import Door.Access.Door8800.Command.Door.Parameter.DoorPort_Parameter;
import Door.Access.Door8800.Command.Door.Result.ReadAutoLockedSetting_Result;
import Door.Access.Door8800.Command.Door8800Command;
import Door.Access.Door8800.Packet.Door8800PacketModel;
import Door.Access.Util.ByteUtil;
import io.netty.buffer.ByteBuf;

/**
 * 定时锁定门，可设定一周内任意时间的门锁定时段。<br>
 * 成功返回结果参考 {@link ReadAutoLockedSetting_Result}
 *
 * @author 赖金杰
 */
public class ReadAutoLockedSetting extends Door8800Command {

    public ReadAutoLockedSetting(DoorPort_Parameter par) {
        _Parameter = par;
        ByteBuf dataBuf = ByteUtil.ALLOCATOR.buffer(1);
        dataBuf.writeByte(par.Door);
        CreatePacket(3, 7, 1, 1, dataBuf);
    }

    @Override
    protected void Release0() {
        return;
    }

    @Override
    protected boolean _CommandStep(INConnectorEvent oEvent, Door8800PacketModel model) {
        if (CheckResponse_Cmd(model, 3, 7, 1, 0xE2)) {
            ByteBuf buf = model.GetDatabuff();

            ReadAutoLockedSetting_Result r = new ReadAutoLockedSetting_Result();
            r.DoorNum = buf.readByte();
            r.Use = buf.readBoolean();
            r.timeGroup.SetBytes(buf);
            //设定返回值
            _Result = r;

            RaiseCommandCompleteEvent(oEvent);
            return true;
        } else {
            return false;
        }
    }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Door.Access.Door8800.Command.Door;

import Door.Access.Connector.INConnectorEvent;
import Door.Access.Door8800.Command.Door.Parameter.DoorPort_Parameter;
import Door.Access.Door8800.Command.Door.Result.ReadInvalidCardAlarmOption_Result;
import Door.Access.Door8800.Command.Door8800Command;
import Door.Access.Door8800.Packet.Door8800PacketModel;
import Door.Access.Util.ByteUtil;
import io.netty.buffer.ByteBuf;

/**
 * 未注册卡读卡时报警功能<br>
 * 成功返回结果参考 {@link ReadInvalidCardAlarmOption_Result}
 *
 * @author 赖金杰
 */
public class ReadInvalidCardAlarmOption extends Door8800Command {

    public ReadInvalidCardAlarmOption(DoorPort_Parameter par) {
        _Parameter = par;
        ByteBuf dataBuf = ByteUtil.ALLOCATOR.buffer(1);
        dataBuf.writeByte(par.Door);
        CreatePacket(3, 0xA, 1, 1, dataBuf);
    }

    @Override
    protected void Release0() {
        return;
    }

    @Override
    protected boolean _CommandStep(INConnectorEvent oEvent, Door8800PacketModel model) {
        if (CheckResponse_Cmd(model, 3, 0xA, 1, 2)) {
            ByteBuf buf = model.GetDatabuff();

            ReadInvalidCardAlarmOption_Result r = new ReadInvalidCardAlarmOption_Result();
            r.DoorNum = buf.readByte();
            r.Use = buf.readBoolean();
            //设定返回值
            _Result = r;

            RaiseCommandCompleteEvent(oEvent);
            return true;
        } else {
            return false;
        }
    }

}

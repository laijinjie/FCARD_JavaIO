/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Door.Access.Door8800.Command.Door;

import Door.Access.Connector.INConnectorEvent;
import Door.Access.Door8800.Command.Door.Parameter.DoorPort_Parameter;
import Door.Access.Door8800.Command.Door.Result.ReadOvertimeAlarmSetting_Result;
import Door.Access.Door8800.Command.Door8800Command;
import Door.Access.Door8800.Packet.Door8800PacketModel;
import Door.Access.Util.ByteUtil;
import io.netty.buffer.ByteBuf;

/**
 * 开门超时报警功能<br/>
 * 门磁打开超过一定时间后就会报警和发出提示语音和响声。<br/>
 * 成功返回结果参考 {@link ReadOvertimeAlarmSetting_Result}
 *
 * @author 赖金杰
 */
public class ReadOvertimeAlarmSetting extends Door8800Command {

    public ReadOvertimeAlarmSetting(DoorPort_Parameter par) {
        _Parameter = par;
        ByteBuf dataBuf = ByteUtil.ALLOCATOR.buffer(1);
        dataBuf.writeByte(par.Door);
        CreatePacket(3, 0xE, 0, 1, dataBuf);
    }

    @Override
    protected void Release0() {
        return;
    }

    @Override
    protected boolean _CommandStep(INConnectorEvent oEvent, Door8800PacketModel model) {
        if (CheckResponse_Cmd(model, 3, 0xE, 0, 5)) {
            ByteBuf buf = model.GetDatabuff();

            ReadOvertimeAlarmSetting_Result r = new ReadOvertimeAlarmSetting_Result();
            r.DoorNum = buf.readByte();
            r.Use = buf.readBoolean();
            r.Overtime = buf.readUnsignedShort();
            r.Alarm = buf.readBoolean();
            //设定返回值
            _Result = r;

            RaiseCommandCompleteEvent(oEvent);
            return true;
        } else {
            return false;
        }
    }

}

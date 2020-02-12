/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Door.Access.Door8800.Command.Door;

import Door.Access.Connector.INConnectorEvent;
import Door.Access.Door8800.Command.Door.Parameter.DoorPort_Parameter;
import Door.Access.Door8800.Command.Door.Result.ReadSensorAlarmSetting_Result;
import Door.Access.Door8800.Command.Door8800Command;
import Door.Access.Door8800.Packet.Door8800PacketModel;
import Door.Access.Util.ByteUtil;
import io.netty.buffer.ByteBuf;

/**
 * 门磁报警功能<br/>
 * 当无有效开门验证时（远程开门、刷卡、密码、出门按钮），检测到门磁打开时就会报警。<br/>
 * 成功返回结果参考 {@link ReadSensorAlarmSetting_Result}
 *
 * @author 赖金杰
 */
public class ReadSensorAlarmSetting extends Door8800Command {

    public ReadSensorAlarmSetting(DoorPort_Parameter par) {
        _Parameter = par;
        ByteBuf dataBuf = ByteUtil.ALLOCATOR.buffer(1);
        dataBuf.writeByte(par.Door);

        CreatePacket(3, 0x10, 0, 1, dataBuf);
    }

    @Override
    protected void Release0() {
        return;
    }

    @Override
    protected boolean _CommandStep(INConnectorEvent oEvent, Door8800PacketModel model) {
        if (CheckResponse_Cmd(model, 3, 0x10, 0, 0xE2)) {
            ByteBuf buf = model.GetDatabuff();

            ReadSensorAlarmSetting_Result r = new ReadSensorAlarmSetting_Result();
            r.DoorNum = buf.readByte();
            r.Use = buf.readBoolean();
            r.TimeGroup.SetBytes(buf);
            //设定返回值
            _Result = r;

            RaiseCommandCompleteEvent(oEvent);
            return true;
        } else {
            return false;
        }
    }

}

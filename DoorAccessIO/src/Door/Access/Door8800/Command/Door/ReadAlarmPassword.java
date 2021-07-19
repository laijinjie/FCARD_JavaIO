/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Door.Access.Door8800.Command.Door;

import Door.Access.Connector.INConnectorEvent;
import Door.Access.Door8800.Command.Door.Parameter.DoorPort_Parameter;
import Door.Access.Door8800.Command.Door.Result.ReadAlarmPassword_Result;
import Door.Access.Door8800.Command.Door8800Command;
import Door.Access.Door8800.Packet.Door8800PacketModel;
import Door.Access.Util.ByteUtil;
import io.netty.buffer.ByteBuf;

/**
 * 胁迫报警功能<br>
 * 功能开启后，在密码键盘读卡器上输入特定密码后就会报警；<br>
 * 成功返回结果参考 {@link ReadAlarmPassword_Result}
 *
 * @author 赖金杰
 */
public class ReadAlarmPassword extends Door8800Command {

    public ReadAlarmPassword(DoorPort_Parameter par) {
        _Parameter = par;
        ByteBuf dataBuf = ByteUtil.ALLOCATOR.buffer(1);
        dataBuf.writeByte(par.Door);
        CreatePacket(3, 0xB, 1, 1, dataBuf);
    }

    @Override
    protected void Release0() {
        return;
    }

    @Override
    protected boolean _CommandStep(INConnectorEvent oEvent, Door8800PacketModel model) {
        if (CheckResponse_Cmd(model, 3, 0xB, 1, 7)) {
            ByteBuf buf = model.GetDatabuff();

            ReadAlarmPassword_Result r = new ReadAlarmPassword_Result();
            r.DoorNum = buf.readByte();
            r.Use = buf.readBoolean();
            byte[] pwd = new byte[4];
            buf.readBytes(pwd, 0, 4);
            r.Password = ByteUtil.ByteToHex(pwd);
            r.AlarmOption = buf.readByte();
            //设定返回值
            _Result = r;

            RaiseCommandCompleteEvent(oEvent);
            return true;
        } else {
            return false;
        }
    }

}

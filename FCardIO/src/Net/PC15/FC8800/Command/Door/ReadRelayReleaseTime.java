/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Net.PC15.FC8800.Command.Door;

import Net.PC15.Connector.INConnectorEvent;
import Net.PC15.FC8800.Command.Door.Parameter.DoorPort_Parameter;
import Net.PC15.FC8800.Command.Door.Result.ReadRelayReleaseTime_Result;
import Net.PC15.FC8800.Command.FC8800Command;
import Net.PC15.FC8800.Packet.FC8800PacketModel;
import Net.PC15.Util.ByteUtil;
import io.netty.buffer.ByteBuf;

/**
 * 开门保持时间<br/>
 * 继电器开锁后释放时间<br/>
 * 成功返回结果参考 {@link ReadRelayReleaseTime_Result}
 *
 * @author 赖金杰
 */
public class ReadRelayReleaseTime extends FC8800Command {

    public ReadRelayReleaseTime(DoorPort_Parameter par) {
        _Parameter = par;
        ByteBuf dataBuf = ByteUtil.ALLOCATOR.buffer(1);
        dataBuf.writeByte(par.Door);
        CreatePacket(3, 8, 0, 1, dataBuf);
    }

    @Override
    protected void Release0() {
        return;
    }

    @Override
    protected boolean _CommandStep(INConnectorEvent oEvent, FC8800PacketModel model) {
        if (CheckResponse_Cmd(model, 3, 1, 0, 4)) {
            ByteBuf buf = model.GetDatabuff();

            ReadRelayReleaseTime_Result r = new ReadRelayReleaseTime_Result();
            r.Door = buf.readByte();
            r.ReleaseTime = buf.readUnsignedShort();
            //设定返回值
            _Result = r;

            RaiseCommandCompleteEvent(oEvent);
            return true;
        } else {
            return false;
        }
    }

}

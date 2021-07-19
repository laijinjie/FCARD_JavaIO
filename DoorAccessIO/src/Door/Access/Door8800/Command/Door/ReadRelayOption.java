/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Door.Access.Door8800.Command.Door;

import Door.Access.Command.CommandParameter;
import Door.Access.Connector.INConnectorEvent;
import Door.Access.Door8800.Command.Door.Result.ReadRelayOption_Result;
import Door.Access.Door8800.Command.Door8800Command;
import Door.Access.Door8800.Packet.Door8800PacketModel;
import io.netty.buffer.ByteBuf;

/**
 * 读取控制器继电器参数<br>
 * <ul>
 * <li>1 &emsp; 不输出（默认）"COM\\u0024NC"</li>
 * <li>2 &emsp; 输出 "COM\\u0024NO";                                                     </li>
 * <li>3 &emsp; 读卡切换输出状态（当读到合法卡后自动自动切换到当前相反的状态。）例如卷闸门。 </li>
 * </ul><br>
 * 成功返回结果参考 {@link ReadRelayOption_Result}
 *
 * @author 赖金杰
 */
public class ReadRelayOption extends Door8800Command {

    public ReadRelayOption(CommandParameter par) {
        _Parameter = par;

        CreatePacket(3, 2);
    }

    @Override
    protected void Release0() {
        return;
    }

    @Override
    protected boolean _CommandStep(INConnectorEvent oEvent, Door8800PacketModel model) {
        if (CheckResponse_Cmd(model, 3, 2, 0, 4)) {
            ByteBuf buf = model.GetDatabuff();

            ReadRelayOption_Result r = new ReadRelayOption_Result();
            for (int i = 1; i <= 4; i++) {
                r.door.SetDoor(i, buf.readByte());
            }
            //设定返回值
            _Result = r;

            RaiseCommandCompleteEvent(oEvent);
            return true;
        } else {
            return false;
        }
    }

}

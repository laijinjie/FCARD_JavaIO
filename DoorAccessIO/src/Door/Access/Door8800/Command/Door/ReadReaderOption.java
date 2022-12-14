/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Door.Access.Door8800.Command.Door;

import Door.Access.Command.CommandParameter;
import Door.Access.Connector.INConnectorEvent;
import Door.Access.Door8800.Command.Door.Result.ReadReaderOption_Result;
import Door.Access.Door8800.Command.Door8800Command;
import Door.Access.Door8800.Packet.Door8800PacketModel;
import io.netty.buffer.ByteBuf;

/**
 * 读取控制器4个门的读卡器字节数<br>
 * 成功返回结果参考 {@link ReadReaderOption_Result}
 *
 * @author 赖金杰
 */
public class ReadReaderOption extends Door8800Command {

    public ReadReaderOption(CommandParameter par) {
        _Parameter = par;

        CreatePacket(3, 1);
    }

    @Override
    protected void Release0() {
        return;
    }

    @Override
    protected boolean _CommandStep(INConnectorEvent oEvent, Door8800PacketModel model) {
        if (CheckResponse_Cmd(model, 3, 1, 0, 4)) {
            ByteBuf buf = model.GetDatabuff();

            ReadReaderOption_Result r = new ReadReaderOption_Result();
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

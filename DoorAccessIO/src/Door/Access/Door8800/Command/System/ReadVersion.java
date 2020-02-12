/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Door.Access.Door8800.Command.System;

import Door.Access.Command.CommandParameter;
import Door.Access.Connector.INConnectorEvent;
import Door.Access.Door8800.Command.Door8800Command;
import Door.Access.Door8800.Command.System.Result.ReadVersion_Result;
import Door.Access.Door8800.Packet.Door8800PacketModel;
import io.netty.buffer.ByteBuf;

/**
 * 获取控制器版本号
 *
 * <p>
 * 成功返回结果参考 {@link ReadVersion_Result}
 *
 * @author 赖金杰
 */
public class ReadVersion extends Door8800Command {

    public ReadVersion(CommandParameter par) {
        _Parameter = par;

        CreatePacket(1, 8);
    }

    @Override
    protected void Release0() {
        return;
    }

    @Override
    protected boolean _CommandStep(INConnectorEvent oEvent, Door8800PacketModel model) {
        if (CheckResponse_Cmd(model, 1, 8, 0, 4)) {
            ByteBuf buf = model.GetDatabuff();
            byte[] strBuf1 = new byte[2];
            byte[] strBuf2 = new byte[2];
            String ver;
            try {
                buf.readBytes(strBuf1, 0, 2);
                buf.readBytes(strBuf2, 0, 2);

                ver = new String(strBuf1) + "." + new String(strBuf2);
            } catch (Exception e) {
                ver = null;
            }

            //设定返回值
            _Result = new ReadVersion_Result(ver);

            RaiseCommandCompleteEvent(oEvent);
            return true;
        } else {
            return false;
        }
    }

}

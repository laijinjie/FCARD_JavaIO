/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Door.Access.Door8800.Command.Password;

import Door.Access.Command.CommandParameter;
import Door.Access.Connector.INConnectorEvent;
import Door.Access.Door8800.Command.Data.PasswordDetail;
import Door.Access.Door8800.Command.Password.Result.ReadPasswordDataBase_Result;
import Door.Access.Door8800.Command.Door8800Command;
import Door.Access.Door8800.Packet.Door8800PacketModel;
import io.netty.buffer.ByteBuf;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * 读取所有开门密码
 *
 * @author F
 */
public class ReadPasswordDataBase extends Door8800Command {

    protected ArrayList<Object> list;
    /**
     * 密码总数
     */
    protected int PasswordCount;

    public ReadPasswordDataBase(CommandParameter par) {
        _Parameter = par;
        PasswordCount = 0;
        list = new ArrayList<>();
        CreatePacket(0x05, 0x03);
    }

    @Override
    protected boolean _CommandStep(INConnectorEvent oEvent, Door8800PacketModel model) {
        if (CheckResponse_Cmd(model, 0x05, 0x03, 0x00)) {
            Analysis(model.GetDatabuff());
            return true;
        }
        if (CheckResponse_Cmd(model, 0x05, 0x03, 0xFF, 0x04)) {
            PasswordCount = model.GetDatabuff().readInt();
            Result();
            RaiseCommandCompleteEvent(oEvent);
            return true;
        }
        return false;
    }

    protected void Analysis(ByteBuf buf) {
        int count = buf.readInt();
        for (int i = 0; i < count; i++) {
            PasswordDetail detail = new PasswordDetail();
            detail.SetBytes(buf);
            list.add(detail);
        }
        buf.release();
    }

    protected void Result() {
        ReadPasswordDataBase_Result result = new ReadPasswordDataBase_Result();
        result.PasswordDetails = list;
        result.DataBaseSize = PasswordCount;
        _Result = result;
    }

    @Override
    protected void Release0() {

    }
}

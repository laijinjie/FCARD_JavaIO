/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Face.Person;

import Door.Access.Command.CommandParameter;
import Door.Access.Connector.INConnectorEvent;
import Door.Access.Door8800.Command.Door8800Command;
import Door.Access.Door8800.Packet.Door8800PacketModel;
import Face.Person.Result.ReadPersonDatabaseDetail_Result;
import io.netty.buffer.ByteBuf;

/**
 * 读取人员存储详情
 *
 * @author F
 */
public class ReadPersonDatabaseDetail extends Door8800Command {
/**
 * 读取人员存储详情
 * @param par 命令参数
 */
    public ReadPersonDatabaseDetail(CommandParameter par) {
        _Parameter = par;
        CreatePacket(7, 1, 0, 0, null);
    }

    @Override
    protected void Release0() {
        return;
    }

    @Override
    protected boolean _CommandStep(INConnectorEvent oEvent, Door8800PacketModel model) {
        if (CheckResponse_Cmd(model,7,1,0,0x18)) {
            ByteBuf buf = model.GetDatabuff();
            ReadPersonDatabaseDetail_Result result = new ReadPersonDatabaseDetail_Result();
            result.SortDataBaseSize = buf.readUnsignedInt();
            result.SortPersonSize = buf.readUnsignedInt();
            result.SortFingerprintDataBaseSize = buf.readUnsignedInt();
            result.SortFingerprintSize = buf.readUnsignedInt();
            result.SortFaceDataBaseSize = buf.readUnsignedInt();
            result.SortFaceSize = buf.readUnsignedInt();
            _Result = result;
            RaiseCommandCompleteEvent(oEvent);
            return true;
        } else {
            return false;
        }
    }
}

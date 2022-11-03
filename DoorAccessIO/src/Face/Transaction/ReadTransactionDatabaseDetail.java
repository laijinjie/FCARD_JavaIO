package Face.Transaction;

import Door.Access.Command.CommandParameter;
import Door.Access.Connector.INConnectorEvent;
import Door.Access.Door8800.Command.Door8800Command;
import Door.Access.Door8800.Packet.Door8800PacketModel;
import Face.Transaction.Result.ReadTransactionDatabaseDetail_Result;

/**
 * 读取控制器中的卡片数据库信息
 */
public class ReadTransactionDatabaseDetail extends Door8800Command {
    /**
     * 读取控制器中的卡片数据库信息
     * @param parameter
     */
    public ReadTransactionDatabaseDetail(CommandParameter parameter) {
        _Parameter=parameter;
        CreatePacket(0x08, 0x01);
    }

    @Override
    protected boolean _CommandStep(INConnectorEvent oEvent, Door8800PacketModel model) {
        boolean bResult = false;
        if (CheckResponse_Cmd(model, 0x08, 0x01)) {
            ReadTransactionDatabaseDetail_Result rst = new ReadTransactionDatabaseDetail_Result();
            rst.setBytes(model.GetDatabuff());
            _Result=rst;
            bResult=true;
            RaiseCommandCompleteEvent(oEvent);
        }
        return bResult;
    }

    @Override
    protected void Release0() {

    }
}

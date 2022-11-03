package Face.Elevator.System.WorkType;

import Door.Access.Command.CommandParameter;
import Door.Access.Connector.INConnectorEvent;
import Door.Access.Door8800.Command.Door8800Command;
import Door.Access.Door8800.Packet.Door8800PacketModel;

/**
 * 读取电梯工作模式
 */
public class ReadWorkType extends Door8800Command {
    /**
     * 创建读取电梯工作模式命令
     * @param parameter 命令参数
     */
    public  ReadWorkType (CommandParameter parameter){
        _Parameter=parameter;
        CreatePacket(0x03, 0x21);
    }

    @Override
    protected boolean _CommandStep(INConnectorEvent oEvent, Door8800PacketModel model) {
        if (CheckResponse_Cmd(model, 1))
        {
            ReadWorkType_Result rst = new ReadWorkType_Result();
            _Result = rst;
            rst.SetBytes(model.GetDatabuff());
            RaiseCommandCompleteEvent(oEvent);
        }

        return  true;
    }

    @Override
    protected void Release0() {

    }
}

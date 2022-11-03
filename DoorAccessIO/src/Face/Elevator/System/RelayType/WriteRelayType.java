package Face.Elevator.System.RelayType;

import Door.Access.Door8800.Command.Door8800Command;
import io.netty.buffer.ByteBuf;

/**
 * 写入电梯继电器板的继电器输出类型
 */
public class WriteRelayType extends Door8800Command {
    /**
     *写入电梯继电器板的继电器输出类型
     * @param parameter 写入电梯继电器板的继电器输出类型的参数
     */
    public  WriteRelayType (WriteRelayType_Parameter parameter){
        _Parameter =parameter;
        ByteBuf buf=parameter.GetBytes();
        CreatePacket(0x03, 0x22, 0x02,buf.readableBytes(),buf);
    }
    @Override
    protected void Release0() {

    }
}

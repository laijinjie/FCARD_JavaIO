package Face.Elevator.System.RelayType;

import Door.Access.Command.CommandDetail;
import Door.Access.Command.CommandParameter;
import Door.Access.Util.ByteUtil;
import io.netty.buffer.ByteBuf;

import java.util.ArrayList;

/**
 * 写入电梯继电器板的继电器输出类型的参数
 */
public class WriteRelayType_Parameter extends CommandParameter {
    /**
     * 固定64个元素，每个元素代表一个继电器输出类型
     * 1、COM_NC常闭（默认值）
     * 2、COM_NO常闭
     */
    public ArrayList<Byte> RelayTypes;

    /**
     *
     * @param detail 命令详情
     * @param relayTypes 继电器输出类型  1、COM_NC常闭（默认值） 2、COM_NO常闭
     */
    public WriteRelayType_Parameter(CommandDetail detail, ArrayList<Byte> relayTypes)   {
        super(detail);
        RelayTypes = relayTypes;
        checkedParameter();
    }

    private void checkedParameter()   {
        int size = RelayTypes.size();
        if (size != 64) {
            throw new RuntimeException("RelayTypes.size not 64");
        }

        for (int i = 0; i < size; i++) {
            byte relay=RelayTypes.get(i);
            if(relay>2||relay<1){
                throw new RuntimeException("RelayTypes.value Reference range 1-2");
            }
        }
    }

    public ByteBuf GetBytes(){
        ByteBuf buf= ByteUtil.ALLOCATOR.buffer(64);
        for (int i = 0; i < 64; i++)
        {
            buf.writeByte(RelayTypes.get(i));
        }
        return  buf;
    }

}

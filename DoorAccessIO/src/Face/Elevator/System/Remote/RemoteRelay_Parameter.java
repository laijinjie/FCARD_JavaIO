package Face.Elevator.System.Remote;

import Door.Access.Command.CommandDetail;
import Door.Access.Command.CommandParameter;
import Door.Access.Util.ByteUtil;
import io.netty.buffer.ByteBuf;

import java.util.ArrayList;

public class RemoteRelay_Parameter extends CommandParameter {
    /**
     *继电器列表，固定64个元素，每个元素代表一个继电器类型
     * 输出类型：
     * 0、不操作此继电器
     * 1、对继电器执行操作
     */
    public ArrayList<Byte> Relays;
    public RemoteRelay_Parameter(CommandDetail detail, ArrayList<Byte> relays)   {
        super(detail);
        Relays=relays;
        checkedParameter();
    }

    private void checkedParameter()   {
        int size = Relays.size();
        if (size != 64) {
            throw new RuntimeException("Relays.size Reference range 64");
        }

        for (int i = 0; i < size; i++) {
            byte relay=Relays.get(i);
            if(relay>1||relay<0){
                throw new RuntimeException("Relays.value Reference range 0-1");
            }
        }
    }
    public ByteBuf GetBytes(){
        ByteBuf buf= ByteUtil.ALLOCATOR.buffer(64);
        for (int i = 0; i < 64; i++)
        {
            buf.writeByte(Relays.get(i));
        }
        return  buf;
    }
}

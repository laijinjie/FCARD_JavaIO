package Face.Elevator.System.ReleaseTime;

import Door.Access.Command.CommandDetail;
import Door.Access.Command.CommandParameter;
import Door.Access.Util.ByteUtil;
import io.netty.buffer.ByteBuf;

import java.util.ArrayList;

public class WriteReleaseTime_Parameter extends CommandParameter {
    public ArrayList<Integer> ReleaseTimes;
    public WriteReleaseTime_Parameter(CommandDetail detail,ArrayList<Integer> releaseTimes)   {
        super(detail);
        ReleaseTimes=releaseTimes;
        checkedParameter();
    }

    public void checkedParameter()  {
        int size = ReleaseTimes.size();
        if (size != 64) {
            throw new RuntimeException("ReleaseTimes.size not 64");
        }

        for (int i = 0; i < size; i++) {
            int relay=ReleaseTimes.get(i);
            if(relay>65535||relay<0){
                throw new RuntimeException("ReleaseTimes.value Reference range 0-65535");
            }
        }
    }

    public ByteBuf GetBytes(){
        ByteBuf buf= ByteUtil.ALLOCATOR.buffer(64);
        for (int i = 0; i < 64; i++)
        {
            buf.writeShort(ReleaseTimes.get(i));
        }
        return  buf;
    }
}

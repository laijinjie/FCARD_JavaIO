package Face.Elevator.System.ReleaseTime;

import Door.Access.Command.INCommandResult;
import io.netty.buffer.ByteBuf;

import java.util.ArrayList;

public class ReadReleaseTime_Result implements INCommandResult {

    public ArrayList<Integer> ReleaseTimes;

    public ReadReleaseTime_Result() {
        ReleaseTimes = new ArrayList<>();
    }

    public  void SetBytes(ByteBuf buf){
        for (int i = 0; i < 64; i++)
        {
            ReleaseTimes.add(buf.readUnsignedShort());
        }
    }

    @Override
    public void release() {

    }
}

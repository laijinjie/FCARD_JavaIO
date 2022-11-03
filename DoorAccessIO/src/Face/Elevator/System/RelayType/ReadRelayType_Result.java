package Face.Elevator.System.RelayType;

import Door.Access.Command.INCommandResult;
import io.netty.buffer.ByteBuf;

import java.util.ArrayList;

public class ReadRelayType_Result implements INCommandResult {

    public ArrayList<Byte> RelayTypes;

    public  ReadRelayType_Result(){
        RelayTypes=new ArrayList<>();
    }
    @Override
    public void release() {

    }
    public  void SetBytes(ByteBuf buf)
    {
        for (int i = 0; i < 64; i++)
        {
            RelayTypes.add(buf.readByte());
        }
    }
}

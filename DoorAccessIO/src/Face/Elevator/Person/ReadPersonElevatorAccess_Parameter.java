package Face.Elevator.Person;

import Door.Access.Command.CommandDetail;
import Door.Access.Command.CommandParameter;
import Door.Access.Util.ByteUtil;
import io.netty.buffer.ByteBuf;

public class ReadPersonElevatorAccess_Parameter extends CommandParameter {
    long UserCode;
    public ReadPersonElevatorAccess_Parameter(CommandDetail detail,long userCode) {
        super(detail);
        UserCode=userCode;
    }

    public ByteBuf GetBytes(){
        ByteBuf buf=  ByteUtil.ALLOCATOR.buffer(4);
        buf.writeInt((int)UserCode);
        return  buf;
    }


}

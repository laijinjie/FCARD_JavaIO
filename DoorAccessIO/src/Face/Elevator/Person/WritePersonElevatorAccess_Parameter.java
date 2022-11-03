package Face.Elevator.Person;

import Door.Access.Command.CommandDetail;
import Door.Access.Command.CommandParameter;
import Door.Access.Util.ByteUtil;
import io.netty.buffer.ByteBuf;

import java.util.ArrayList;

public class WritePersonElevatorAccess_Parameter extends CommandParameter {
    /**
     * 用户编号
     */
    public int UserCode;

    /***
     *继电器权限列表，固定64个元素，每个元素代表一个继电器权限 权限说明：0表示无权限，1表示有权限
     */
    public  ArrayList<Byte> RelayAccesss;

    public WritePersonElevatorAccess_Parameter(CommandDetail detail) {
        super(detail);
        RelayAccesss=new ArrayList<>(64);
    }


    protected void CheckParameter()  {
        if(_Detail ==null){
            throw new RuntimeException("CommandDetail is null");
        }
        if(UserCode<=0){
            throw new RuntimeException("UserCode error");
        }
        if(RelayAccesss==null){
            throw new RuntimeException("RelayAccesss is null");
        }
        if(RelayAccesss.size()!=64){
            throw new RuntimeException("RelayAccesss The size must be 64 ");
        }
    }

    public ByteBuf GetBytes(){
        ByteBuf buf=  ByteUtil.ALLOCATOR.buffer(68);
        buf.writeInt(UserCode);
        for (int i = 0; i < 64; i++)
        {
            buf.writeByte(RelayAccesss.get(i));
        }
        return  buf;
    }
}

package Face.Elevator.Person;

import Door.Access.Command.INCommandResult;
import io.netty.buffer.ByteBuf;

public class WritePersonElevatorAccess_Result implements INCommandResult {

    /***
     * 用户号
     */
  public   long UserCode;
    /**
     * 状态 1--表示成功；0--表示用户号未登记；2--表示不支持此功能
     */
    public  byte Status;

    @Override
    public void release() {

    }

    public  void SetBytes(ByteBuf buf){
        UserCode = buf.readUnsignedInt();
        Status = buf.readByte();
    }
}

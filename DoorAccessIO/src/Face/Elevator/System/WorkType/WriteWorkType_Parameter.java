package Face.Elevator.System.WorkType;

import Door.Access.Command.CommandDetail;
import Door.Access.Command.CommandParameter;
import Door.Access.Util.ByteUtil;
import io.netty.buffer.ByteBuf;

/**
 * 设置定时常开参数的命令 参数
 */
public class WriteWorkType_Parameter extends CommandParameter {

    /**
     * 电梯工作模式
     * 0--禁用电梯模式
     * 1--启动电梯模式
     */
    public byte WorkType;

    public WriteWorkType_Parameter(CommandDetail detail,int workType) {
        super(detail);
        WorkType=(byte)workType;
        checkedParameter();
    }

    public  void checkedParameter()
    {
        if (WorkType < 0 || WorkType > 1)
            throw  new RuntimeException("Port reference range    0- 1 ");
    }
    public ByteBuf GetBytes(){
        ByteBuf buf= ByteUtil.ALLOCATOR.buffer(1);
        buf.writeByte(WorkType);
        return  buf;
    }
}

package Face.Elevator.System.TimingOpen;

import Door.Access.Command.CommandDetail;
import Door.Access.Command.CommandParameter;
import Door.Access.Door8800.Command.Data.TimeGroup.WeekTimeGroup;
import Door.Access.Util.ByteUtil;
import io.netty.buffer.ByteBuf;

public class WriteTimingOpen_Parameter extends CommandParameter {

    /**
     * 端口号
     */
    public  byte Port;
    /***
     *是否启用
     * 1--启用;0--禁用
     */
    public  byte Use;
    /**
     *常开工作模式
     * 1--合法认证通过后在指定时段内即可常开
     * 2---授权中标记为常开特权的在指定时段内认证通过即可常开
     * 3--自动开关，到时间自动开关门。
     */
    public  byte WorkType;

    public Door.Access.Door8800.Command.Data.TimeGroup.WeekTimeGroup WeekTimeGroup;

    public WriteTimingOpen_Parameter(CommandDetail detail) {
        super(detail);
        WeekTimeGroup =new WeekTimeGroup(8);

    }

    public  void checkedParameter()
    {
        if (Port < 1 || Port > 64)
            throw  new RuntimeException("Port reference range    1- 64 ");
        if (Use < 0 || Use > 1)
            throw  new RuntimeException("Use reference range 0-1 ");
        if (WorkType < 1 || WorkType > 3)
            throw  new RuntimeException("WorkType reference range 1-3 ");
    }

    public ByteBuf GetBytes(){
        ByteBuf buf= ByteUtil.ALLOCATOR.buffer(0xE3);
        buf.writeByte(Port);
        buf.writeByte(Use);
        buf.writeByte(WorkType);
        WeekTimeGroup.GetBytes(buf);
        return  buf;
    }
}

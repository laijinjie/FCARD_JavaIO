package Face.Elevator.System.TimingOpen;

import Door.Access.Command.INCommandResult;
import Door.Access.Door8800.Command.Data.TimeGroup.WeekTimeGroup;
import io.netty.buffer.ByteBuf;

public class ReadTimingOpen_Result implements INCommandResult {
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

    public WeekTimeGroup WeekTimeGroup;

    public  ReadTimingOpen_Result(){
        WeekTimeGroup =new WeekTimeGroup(8);
    }

    public  void SetBytes(ByteBuf databuf)  {
        if (databuf.readableBytes() < 0xE3)
        {
            throw new RuntimeException("databuf Error");
        }
        if (WeekTimeGroup == null)
        {
            WeekTimeGroup = new WeekTimeGroup(8);
        }
        Port = databuf.readByte();
        Use = databuf.readByte();
        WorkType = databuf.readByte();
        WeekTimeGroup.SetBytes(databuf);
    }
    @Override
    public void release() {

    }
}

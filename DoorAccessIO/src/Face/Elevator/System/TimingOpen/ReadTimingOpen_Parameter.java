package Face.Elevator.System.TimingOpen;

import Door.Access.Command.CommandDetail;
import Door.Access.Command.CommandParameter;

/**
 * 读取定时常开参数命令的参数
 */
public class ReadTimingOpen_Parameter extends CommandParameter {
    byte Port;
    public ReadTimingOpen_Parameter(CommandDetail detail,byte port)  {
        super(detail);
        Port=port;
        checkedParameter();
    }
    public  void checkedParameter()  {
        if (Port < 1 || Port > 64)
        {
            throw new RuntimeException("Port Reference range 1-64");
        }
    }
}

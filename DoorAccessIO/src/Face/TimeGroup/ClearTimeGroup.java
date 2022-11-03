package Face.TimeGroup;

import Door.Access.Command.CommandParameter;
import Door.Access.Door8800.Command.Door8800Command;

/**
 * 清空开门时段
 */
public class ClearTimeGroup extends Door8800Command {
    /**
     * 初始化对象
     * @param parameter 命令参数
     */
    public ClearTimeGroup (CommandParameter parameter){
        _Parameter=parameter;
        CreatePacket(0x06, 0x01);
    }
    @Override
    protected void Release0() {

    }
}

package Face.Person;

import Door.Access.Command.CommandParameter;
import Door.Access.Door8800.Command.Door8800Command;

/**
 * 从控制器中清空所有人员信息
 *
 * @author F
 */
public class ClearPersonDataBase extends Door8800Command {
    /**
     * 从控制器中清空所有人员信息
     * @param parameter 命令参数
     */
    public ClearPersonDataBase(CommandParameter parameter) {
        _Parameter = parameter;
        CreatePacket(0x07, 0x02);
    }

    @Override
    protected void Release0() {

    }
}

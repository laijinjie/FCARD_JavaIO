package Face.System;

import Door.Access.Command.CommandParameter;
import Door.Access.Door8800.Command.Door8800Command;

/**
 * 开启数据监控
 */
public class BeginWatch extends Door8800Command {
    /**
     * 开启数据监控
     *
     * @param parameter 连接参数
     */
    public BeginWatch(CommandParameter parameter) {
        _Parameter = parameter;
        CreatePacket(0x01, 0x0b, 0x00);
    }

    @Override
    protected void Release0() {

    }
}

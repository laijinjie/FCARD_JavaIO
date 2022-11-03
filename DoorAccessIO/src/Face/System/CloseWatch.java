package Face.System;

import Door.Access.Command.CommandParameter;
import Door.Access.Door8800.Command.Door8800Command;

/**
 * 关闭数据监控
 */
public class CloseWatch extends Door8800Command {
    /**
     * 关闭数据监控
     * @param parameter 连接参数
     */
    public CloseWatch(CommandParameter parameter) {
        _Parameter = parameter;
        CreatePacket(0x01, 0x0b, 0x01);
    }

    @Override
    protected void Release0() {

    }
}

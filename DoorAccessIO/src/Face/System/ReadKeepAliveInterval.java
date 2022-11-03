package Face.System;

import Door.Access.Command.CommandParameter;
import Door.Access.Door8800.Command.Door8800Command;

/**
 * 读取客户端包活间隔
 */
public class ReadKeepAliveInterval extends Door8800Command {
    /**
     * 读取客户端包活间隔
     * @param parameter
     */
    public ReadKeepAliveInterval(CommandParameter parameter) {
        _Parameter = parameter;

    }

    @Override
    protected void Release0() {

    }
}

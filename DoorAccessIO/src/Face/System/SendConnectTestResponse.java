package Face.System;

import Door.Access.Command.CommandParameter;
import Door.Access.Door8800.Command.Door8800Command;

/**
 * 心跳包与连接测试响应
 */
public class SendConnectTestResponse extends Door8800Command {

    public SendConnectTestResponse(CommandParameter parameter) {
        _Parameter = parameter;
        CreatePacket(0x19, 0xA0, 0xA0);
        _IsWaitResponse=false;
    }
    @Override
    protected void Release0() {

    }
}

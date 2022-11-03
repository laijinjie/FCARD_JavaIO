package Face.System.Parameter;

import Door.Access.Command.CommandDetail;
import Door.Access.Command.CommandParameter;
import io.netty.buffer.ByteBuf;

/**
 * 写入客户端模式通讯方式_参数
 */
public class WriteClientWorkMode_Parameter extends CommandParameter {
    /**
     * 客户端模式通讯方式<br>
     * 0--禁用;<br>
     * 1--UDP;<br>
     * 2--TCP Client;<br>
     * 3--TCP Client + TLS ;<br>
     * 4--MQTT（TCP Client）;<br>
     * 5--MQTT（TCP Client） + TLS ;<br>
     */
    public int ClientModel;

    /**
     * 写入客户端模式通讯方式_参数
     */
    public WriteClientWorkMode_Parameter(CommandDetail detail){
        super(detail);
        ClientModel=0;
    }

    /**
     * 编码参数 (内部使用)
     * @param buf
     */
    public void GetBytes(ByteBuf buf) {
        buf.writeByte(ClientModel);
    }

    /**
     * 编码参数(内部使用)
     * @param buf
     */
    public void SetBytes(ByteBuf buf) {
        ClientModel = buf.readByte();
    }
}

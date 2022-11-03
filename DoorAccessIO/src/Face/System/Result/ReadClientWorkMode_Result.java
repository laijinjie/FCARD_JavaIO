package Face.System.Result;

import Door.Access.Command.INCommandResult;
import io.netty.buffer.ByteBuf;

/**
 * 读取客户端模式通讯方式——返回结果
 */
public class ReadClientWorkMode_Result implements INCommandResult {
    /**
     * 客户端模式通讯方式:<br>
     * 0--禁用;<br>
     * 1--UDP;<br>
     * 2--TCP Client;<br>
     * 3--TCP Client + TLS ;<br>
     * 4--MQTT（TCP Client）;<br>
     * 5--MQTT（TCP Client） + TLS ;  <br>
     */
    public int ClientModel;

    /**
     * 读取客户端模式通讯方式-返回结果
     */
    public ReadClientWorkMode_Result() {
        ClientModel = 0;
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

    @Override
    public void release() {

    }
}

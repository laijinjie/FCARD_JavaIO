package Face.System.Result;

import Door.Access.Command.INCommandResult;
import io.netty.buffer.ByteBuf;

/**
 * 命令设备立刻重新连接服务器-返回结果
 */
public class RequireConnectServer_Result implements INCommandResult {

    /**
     * 保活包状态返回值<br>
     * 1--已重新连接（UDP时表示已发送保活包）<br>
     * 2--Server 参数未设置<br>
     * 3--Server 参数错误<br>
     * 4--Server 连接失败 （TCP）<br>
     * 5--服务器无应答<br>
     * 6--网络参数设置错误<br>
     * 7--网线未连接<br>
     * 8--Wifi 未连接<br>
     */
    public  int ResultStatus;

    /**
     * 命令设备立刻重新连接服务器-返回结果
     */
    public RequireConnectServer_Result(){
        ResultStatus=0;
    }
    /**
     * 解码参数(内部使用)
     * @param dataBuf
     */
    public void SetBytes(ByteBuf dataBuf)
    {
        ResultStatus = dataBuf.readByte();
    }

    @Override
    public void release() {

    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Door.Access.Command;

import Door.Access.Connector.INConnectorEvent;
import Door.Access.Packet.INPacket;
import io.netty.buffer.ByteBuf;

/**
 * 命令运行时接口，此接口负责和连接器进行交互
 *
 * @author 赖金杰
 */
public interface INCommandRuntime {

    /**
     * 释放资源
     */
    public void Release();

    /**
     * 指示此命令是否需要等待应答
     *
     * @return
     */
    public boolean getIsWaitResponse();

    /**
     * 检查接收的响应数据是否是针对此命令的
     *
     * @param oEvent
     * @param bData
     * @return
     */
    public boolean CheckResponse(INConnectorEvent oEvent, ByteBuf bData);

    /**
     * 检查此命令是否超时
     *
     * @param oEvent
     * @return
     */
    public boolean CheckTimeout(INConnectorEvent oEvent);

    /**
     * 获取一个数据包，用于发送到通讯链路上。
     *
     * @return
     */
    public INPacket GetPacket();

    /**
     * 指示当前指令的状态
     *
     * @return
     */
    public E_CommandStatus GetStatus();

    /**
     * 当命令中的数据被发送出去后会被调用此方法，以更改命令当前的状态
     */
    public void SendCommand(INConnectorEvent oEvent);

    /**
     * 获取此命令是否已完结
     *
     * @return true表示已成功发送
     */
    public boolean getIsCommandOver();

    /**
     * 获取命令中的身份信息
     *
     * @return
     */
    public INIdentity GetIdentity();
    
    
    public void RaiseCommandProcessEvent(INConnectorEvent oEvent);
    public void RaiseCommandCompleteEvent(INConnectorEvent oEvent);
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Net.PC15.Connector;

import Net.PC15.Command.CommandDetail;
import Net.PC15.Command.INCommand;
import Net.PC15.Command.INCommandResult;
import Net.PC15.Connector.TCPServer.TCPServerClientDetail;
import Net.PC15.Data.INData;
import io.netty.buffer.ByteBuf;

/**
 * 当通讯连接器有事件需要通知时，调用对应的事件函数。
 *
 * @author 赖金杰
 */
public interface INConnectorEvent {

    /**
     * 当命令完成时，会触发此函数回调
     *
     * @param cmd 此次事件所关联的命令详情
     * @param result 命令包含的结果
     */
    public void CommandCompleteEvent(INCommand cmd, INCommandResult result);

    /**
     * 命令进程
     *
     * @param cmd
     */
    public void CommandProcessEvent(INCommand cmd);

    /**
     * 连接通道发生错误时回调
     *
     * @param cmd
     * @param isStop 是否是用户停止时发生的？
     */
    public void ConnectorErrorEvent(INCommand cmd, boolean isStop);

    /**
     * 连接通道发生错误时回调
     *
     * @param detail
     */
    public void ConnectorErrorEvent(ConnectorDetail detail);

    /**
     * 命令超时时，触发此回到函数
     *
     * @param cmd 此命令的内容
     */
    public void CommandTimeout(INCommand cmd);

    /**
     * 通讯密码错误
     *
     * @param cmd
     */
    public void PasswordErrorEvent(INCommand cmd);

    /**
     * 通讯校验和错误
     *
     * @param cmd
     */
    public void ChecksumErrorEvent(INCommand cmd);

    /**
     * 监控数据响应
     */
    public void WatchEvent(ConnectorDetail detail, INData event);
    
    
    /**
     * 客户端上线
     */
    public void ClientOnline(TCPServerClientDetail client);
    
    /**
     * 客户端离线
     */
    public void ClientOffline(TCPServerClientDetail client);
}

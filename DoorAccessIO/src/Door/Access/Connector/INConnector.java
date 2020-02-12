/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Door.Access.Connector;

import Door.Access.Command.INCommand;
import Door.Access.Command.INIdentity;
import Door.Access.Command.INWatchResponse;

/**
 * 所有连接器的抽象类
 *
 * @author 赖金杰
 */
public interface INConnector extends Runnable {

    /**
     * 获取此通道的连接器类型
     *
     * @return 连接器类型
     */
    public E_ConnectorType GetConnectorType();

    /**
     * 获取通道中的命令队列数量
     *
     * @return 命令队列数量
     */
    public int GetCommandCount();

    /**
     * 将一个命令添加到本通道的命令队列中
     *
     * @param cmd 命令
     */
    public void AddCommand(INCommand cmd);

    /**
     * *
     * 当需要解析监控指令时，添加数据包解析器到解析器列表中
     *
     * @param decompile 数据包解析器
     */
    public void AddWatchDecompile(INWatchResponse decompile);

    /**
     * 释放连接器所使用的资源。 包括断开连接，释放命令队列，释放缓冲区。
     */
    public void Release();

    /**
     * 判断此通道是否保持连接，即通道在发送完毕命令后保持连接
     *
     * @return true 表示通道保持打开
     */
    public boolean IsForciblyConnect();

    /**
     * 设定此连接器通道为保持打开状态
     */
    public void OpenForciblyConnect();

    /**
     * 禁止此连接器通道为保持连接状态，即命令发送完毕后关闭连接。
     */
    public void CloseForciblyConnect();

    /**
     * 确定通道是否已失效
     */
    public boolean IsInvalid();

    /**
     * 设置此连接通道所关联的事件触发器
     *
     * @param oEvent 事件触发器
     */
    public void SetEventHandle(INConnectorEvent oEvent);

    /**
     * 获取此连接通道的状态
     *
     * @return
     */
    public E_ConnectorStatus GetStatus();

    /**
     * 停止指定类型的命令，终止命令继续执行
     *
     * @param oEvent 事件触发器
     * @param dtl 命令详情
     */
    public void StopCommand(INIdentity dtl);

    /**
     * 用于异步推动当前的通道中的命令进度。
     */
    @Override
    public void run();

    /**
     * 用来指示任务是否已经加入任务队列，正在排队等待或者正在执行中
     */
    public boolean TaskIsBegin();
    
    /**
     * 当此通道已加入任务队列时，需要调用此函数来改变此通道的任务状态
     */
    public void SetTaskIsBegin();

}

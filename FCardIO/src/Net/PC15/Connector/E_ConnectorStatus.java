/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Net.PC15.Connector;

/**
 * 指示连接通道的状态，已关闭，正在连接，已连接，连接错误
 *
 * @author 赖金杰
 */
public enum E_ConnectorStatus {
    /**
     * 已关闭或未打开连接
     */
    OnClosed(0),
    /**
     * 正在连接
     */
    OnConnecting(1),
    /**
     * 已连接
     */
    OnConnected(2),
    /**
     * 连接时发生错误
     */
    OnError(3),
    /**
     * 连接超时
     */
    OnConnectTimeout(4),
    /**
     * 正在关闭
     */
    OnClosing(5);
    

    private final int value;
    //构造器默认也只能是private, 从而保证构造函数只能在内部使用
    E_ConnectorStatus(int value) {
        this.value = value;
    }

    /**
     * 获取枚举值
     *
     * @return 类型代码
     */
    public int getValue() {
        return value;
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Net.PC15.Connector.TCPClient;

import Net.PC15.Connector.ConnectorDetail;
import Net.PC15.Connector.E_ConnectorType;

/**
 * TCP 客户端的连接器详情
 *
 * @author 赖金杰
 */
public class TCPClientDetail extends ConnectorDetail {

    /**
     * 远程服务器的IP或域名
     */
    public String IP;
    /**
     * 远程服务器的监听端口
     */
    public int Port;

    public TCPClientDetail(String ip, int port) {
        IP = ip;
        Port = port;

    }

    @Override
    public E_ConnectorType GetConnectorType() {
        return E_ConnectorType.OnTCPClient;
    }

    @Override
    public TCPClientDetail clone() throws CloneNotSupportedException {
        TCPClientDetail c = (TCPClientDetail) super.clone();
        c.IP = IP;
        c.Port = Port;
        return c;
    }
}

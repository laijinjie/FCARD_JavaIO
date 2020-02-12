/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Door.Access.Connector.UDP;

import Door.Access.Connector.ConnectorDetail;
import Door.Access.Connector.E_ConnectorType;
import Door.Access.Connector.TCPClient.TCPClientDetail;

/**
 * UDP连接器，只是对端的IP和端口，进行UDP连接
 *
 * @author 赖金杰
 */
public class UDPDetail extends ConnectorDetail {

    /**
     * 远程设备的IP或域名 -- UDP
     */
    public String IP;
    /**
     * 远程设备的UDP绑定端口
     */
    public int Port;

    /**
     * 本地绑定IP
     */
    public String LocalIP;

    /**
     * 本地绑定端口
     */
    public int LocalPort;

    /**
     * 指示使用UDP广播包来发送此命令
     */
    public boolean Broadcast;

    public UDPDetail(String ip, int port) {
        IP = ip;
        Port = port;

    }

    @Override
    public E_ConnectorType GetConnectorType() {
        return E_ConnectorType.OnUDP;
    }

    @Override
    public UDPDetail clone() throws CloneNotSupportedException {
        UDPDetail c = (UDPDetail) super.clone();
        c.IP = IP;
        c.Port = Port;
        c.Broadcast = Broadcast;
        c.LocalIP = LocalIP;
        c.LocalPort = LocalPort;
        return c;
    }
}

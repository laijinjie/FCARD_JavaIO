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

    public UDPDetail(String sRemoteIP, int iRemotePort,
            String sLocalIP, int iLocalPort) {
        IP = sRemoteIP;
        Port = iRemotePort;
        LocalIP = sLocalIP;
        LocalPort = iLocalPort;

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
        c.LocalIP = LocalIP;
        c.LocalPort = LocalPort;
        return c;
    }

    @Override
    public String toString() {
        StringBuilder keybuf = new StringBuilder(200);
        keybuf.append("UDPDetail: Local:");
        if (LocalIP != null && !LocalIP.isEmpty()) {
            keybuf.append(LocalIP);
        }
        keybuf.append(":");
        keybuf.append(LocalPort);
        keybuf.append("----: Remote:");
        keybuf.append(IP);
        keybuf.append(":");
        keybuf.append(Port);
        return keybuf.toString();
    }

    public String getLocalKey() {
        StringBuilder keybuf = new StringBuilder(100);
        keybuf.append("UDPLocal:");
        if (LocalIP != null && !LocalIP.isEmpty()) {
            keybuf.append(LocalIP);
        }
        keybuf.append(":");
        keybuf.append(LocalPort);
        return keybuf.toString();
    }

    public String getClientKey() {
        StringBuilder keybuf = new StringBuilder(100);
        keybuf.append("UDPClient:");
        keybuf.append(IP);
        keybuf.append(":");
        keybuf.append(Port);
        return keybuf.toString();
    }

}

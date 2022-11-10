/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Door.Access.Connector.TCPServer;

import Door.Access.Connector.ConnectorDetail;
import Door.Access.Connector.E_ConnectorType;

/**
 * 指示使用连接通道位于本地 TCP Server 的客户端中，根据 ClientID 查询。
 *
 * @author 赖金杰
 */
public class TCPServerClientDetail extends ConnectorDetail {

    /**
     * 客户端通道ID
     */
    public int ClientID;
    /**
     * 服务器本地IP信息(仅供查看，不可修改)
     */
    public IPEndPoint Local;
    /**
     * 远程计算机IP信息(仅供查看，不可修改)
     */
    public IPEndPoint Remote;

    public TCPServerClientDetail(int id) {
        ClientID = id;
    }

    @Override
    public E_ConnectorType GetConnectorType() {
        return E_ConnectorType.OnTCPServer_Client;
    }

    @Override
    public TCPServerClientDetail clone() throws CloneNotSupportedException {
        TCPServerClientDetail c = (TCPServerClientDetail) super.clone();
        c.ClientID = ClientID;
        c.Remote = Remote;
        c.Local = Local;
        return c;
    }

    @Override
    public String toString() {
        StringBuilder keybuf = new StringBuilder(200);
        keybuf.append("TCPServer_Client: ClientID:").append(ClientID);
        if (Local != null) {
            keybuf.append(",Local:");
            keybuf.append(Local.toString());
        }
        if (Remote != null) {
            keybuf.append(",Remote:");
            keybuf.append(Remote.toString());
        }
        return keybuf.toString();
    }
}

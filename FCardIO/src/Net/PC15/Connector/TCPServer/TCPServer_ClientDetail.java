/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Net.PC15.Connector.TCPServer;

/**
 * 表示TCPServer下的子节点（客户端信息）
 * @author 赖金杰
 */
public class TCPServer_ClientDetail extends  IPEndPoint {
    private int _ClientID;
    
    /**
     * 客户端身份信息
     * @param ip 客户端IP
     * @param port 客户端端口
     * @param client 客户端ID号
     */
    public TCPServer_ClientDetail(String ip, int port,int client) {
        super(ip, port);
        _ClientID = client;
    }
    
    /**
     * 客户端ID
     * @return 客户端ID
     */
    public int ClientID()
    {
        return _ClientID;
    }
    
    
}

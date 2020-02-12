/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Door.Access.Connector.TCPServer;

/**
 * 代表一个IP地址和网络端口信息
 * @author 赖金杰
 */
public class IPEndPoint {
    private String _IP;
    private int _Port;
    
    public IPEndPoint(String ip,int port)
    {
        this._IP=ip;
        this._Port=port;
    }
    
    public String IP()
    {
        return _IP;
    }
    
    public int Port()
    {
        return _Port;
    }
    
    public String toString()
    {
        StringBuilder sb=new StringBuilder();
        sb.append(_IP);
        sb.append(":");
        sb.append(_Port);
        return sb.toString();
        
    }
}

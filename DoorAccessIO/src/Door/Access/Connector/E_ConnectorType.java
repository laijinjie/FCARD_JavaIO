/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Door.Access.Connector;

/**
 * 指示连接器的类型
 * @author 赖金杰
 */
public enum E_ConnectorType {
    /**
     * 使用 COM 进行 RS232、RS485 通讯
     */
    OnComm(0),
    /**
     * 使用 TCP 协议，作为客户端连接到指定IP和端口的服务器
     */
    OnTCPClient(1),
    /**
     * 使用 TCP 协议，在本地服务器中查询已连接到的客户端，需要指定客户端ID号。
     */
    OnTCPServer_Client(2),
    /**
     * 使用 UDP 协议，发送数据到指定IP和端口，可指定本地绑定的IP和端口。
     */
    OnUDP(3),
    /**
     * 将需要指令写入到指定的文件地址中，需要指定文件路径和名称，并确保有可写权限。
     */
    OnFile(4),
    /**
     * 使用 UDP 协议，在本地服务器中查询已连接到的客户端，需要指定客户端ID号。
     */
    OnUDPServer_Client(5);
    private final int value;

    //构造器默认也只能是private, 从而保证构造函数只能在内部使用
    E_ConnectorType(int value) {
        this.value = value;
    }
    
    /**
     * 获取枚举值
     * @return 类型代码
     */
    public int getValue() {
        return value;
    }
    
}

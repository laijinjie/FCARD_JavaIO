/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Door.Access.Connector;

/**
 * 连接指示类，用来告诉连接工厂应该使用哪种连接器连接对端。
 *
 * @author 赖金杰
 */
public abstract class ConnectorDetail implements Cloneable {

    /**
     * 获取通讯连接所以使用的连接器类型
     *
     * @return 连接器类型枚举
     */
    public abstract E_ConnectorType GetConnectorType();

    /**
     * 连接器连接到对端时最大等待时间，单位毫秒
     */
    public int Timeout;

    /**
     * 连接器连接到对端失败后，最大重试次数。
     */
    public int RestartCount;

    public ConnectorDetail() {
        Timeout = 5000;//默认值是5秒
        RestartCount = 2;//默认重试3次
    }

    @Override
    public ConnectorDetail clone() throws CloneNotSupportedException {
        ConnectorDetail c = (ConnectorDetail) super.clone();
        c.RestartCount = RestartCount;
        c.Timeout = Timeout;
        return c;
    }
}

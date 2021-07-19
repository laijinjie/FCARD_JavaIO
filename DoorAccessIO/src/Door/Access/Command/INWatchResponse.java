/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Door.Access.Command;

import Door.Access.Connector.ConnectorDetail;
import Door.Access.Connector.INConnectorEvent;
import io.netty.buffer.ByteBuf;

/**
 * 监控命令的响应处理
 *
 * @author 赖金杰
 */
public interface INWatchResponse {

    /**
     * 释放资源
     */
    public void Release();

    /**
     * 检查接收的响应数据是否是针对此命令的
     *
     * @param oEvent
     * @param bData
     */
    public void CheckResponse(ConnectorDetail connectorDetail, INConnectorEvent oEvent, ByteBuf bData);
}

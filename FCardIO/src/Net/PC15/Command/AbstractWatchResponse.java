/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Net.PC15.Command;

import Net.PC15.Connector.ConnectorDetail;
import Net.PC15.Connector.INConnectorEvent;
import Net.PC15.Packet.INPacketDecompile;
import Net.PC15.Packet.INPacketModel;
import io.netty.buffer.ByteBuf;
import java.util.ArrayList;

/**
 * 监控命令响应的抽象类
 *
 * @author 赖金杰
 */
public abstract class AbstractWatchResponse implements INWatchResponse {

    final INPacketDecompile _Decompile;

    protected AbstractWatchResponse(INPacketDecompile decompile) {
        _Decompile = decompile;
    }

    @Override
    public void Release() {
        _Decompile.ClearBuf();
        _Decompile.Release();
    }

    @Override
    public void CheckResponse(ConnectorDetail connectorDetail, INConnectorEvent oEvent, ByteBuf bData) {
        ArrayList<INPacketModel> oRetPack = new ArrayList<INPacketModel>(10);
        boolean decompile = false;
        bData.markReaderIndex();
        decompile = _Decompile.Decompile(bData, oRetPack);
        if (decompile) {
            int iLen = oRetPack.size();
            for (int i = 0; i < iLen; i++) {
                INPacketModel model = oRetPack.get(i);
                fireWatchEvent(connectorDetail, oEvent, model
                );
                model.Release();
            }
        }
        bData.resetReaderIndex();
    }

    /**
     * 触发监控事件
     */
    protected abstract void fireWatchEvent(ConnectorDetail connectorDetail, INConnectorEvent oEvent, INPacketModel model);

}

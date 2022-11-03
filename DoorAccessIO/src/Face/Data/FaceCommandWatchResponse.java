package Face.Data;

import Door.Access.Connector.ConnectorDetail;
import Door.Access.Connector.INConnectorEvent;
import Door.Access.Door8800.Command.Data.DefinedTransaction;
import Door.Access.Door8800.Command.Data.Door8800WatchTransaction;
import Door.Access.Door8800.Command.Door8800CommandWatchResponse;
import Door.Access.Door8800.Packet.Door8800PacketModel;
import Door.Access.Packet.INPacketModel;
import Door.Access.Util.UInt32Util;
import io.netty.buffer.ByteBuf;

import java.util.Calendar;

/**
 * 监控数据响应解析
 */
public class FaceCommandWatchResponse extends Door8800CommandWatchResponse {
    /**
     * 监控数据解析
     * @param connectorDetail
     * @param oEvent
     * @param model
     */
    @Override
    protected void fireWatchEvent(ConnectorDetail connectorDetail, INConnectorEvent oEvent, INPacketModel model) {
        Door8800PacketModel packet = (Door8800PacketModel) model;
        if (packet.GetCode() == UInt32Util.UINT32_MAX) {
            if (packet.GetCmdType() == 0x19) {
                Door8800WatchTransaction watchEvent = new Door8800WatchTransaction();
                watchEvent.CmdType = packet.GetCmdType();
                watchEvent.CmdIndex = packet.GetCmdIndex();
                watchEvent.CmdPar = packet.GetCmdPar();
                watchEvent.SN = packet.GetSN();
                DefinedTransaction dt;
                switch (watchEvent.CmdIndex) {
                    case 1://认证记录
                        ByteBuf buf = packet.GetDatabuff();
                        Face.Data.CardTransaction card = new Face.Data.CardTransaction();
                        card.SetBytes(buf);
                        watchEvent.EventData = card;
                        break;
                    case 2://门磁记录
                        DoorSensorTransaction DoorSensor = new DoorSensorTransaction();
                        DoorSensor.SetBytes(packet.GetDatabuff());
                        watchEvent.EventData = DoorSensor;
                        break;
                    case 3://系统记录
                        SystemTransaction System = new SystemTransaction();
                        System.SetBytes(packet.GetDatabuff());
                        watchEvent.EventData = System;
                        break;
                    case 4://体温记录
                        BodyTemperatureTransaction BodyTemp = new BodyTemperatureTransaction();
                        BodyTemp.SetBytes(packet.GetDatabuff());
                        watchEvent.EventData = BodyTemp;
                        break;
                    default:
                        dt = new DefinedTransaction(watchEvent.CmdIndex, 0, Calendar.getInstance());
                        if (packet.GetDataLen() > 0) {
                            dt.SetWatchData(packet.GetDatabuff());
                        }
                        watchEvent.EventData = dt;
                }
                oEvent.WatchEvent(connectorDetail, watchEvent);
            }
        }
    }
}

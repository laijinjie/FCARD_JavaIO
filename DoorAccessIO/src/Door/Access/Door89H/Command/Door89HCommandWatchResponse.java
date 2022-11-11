/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Door.Access.Door89H.Command;

import Door.Access.Command.AbstractWatchResponse;
import Door.Access.Connector.ConnectorDetail;
import Door.Access.Connector.INConnectorEvent;
import Door.Access.Data.KeepAliveTransaction;
import Door.Access.Door8800.Command.Data.AlarmTransaction;
import Door.Access.Door8800.Command.Data.ButtonTransaction;
import Door.Access.Door89H.Command.Data.CardTransaction;
import Door.Access.Door8800.Command.Data.DefinedTransaction;
import Door.Access.Door8800.Command.Data.DoorSensorTransaction;
import Door.Access.Door8800.Command.Data.Door8800WatchTransaction;
import Door.Access.Door8800.Command.Data.SoftwareTransaction;
import Door.Access.Door8800.Command.Data.SystemTransaction;
import Door.Access.Packet.INPacketModel;
import Door.Access.Door8800.Packet.Door8800Decompile;
import Door.Access.Door8800.Packet.Door8800PacketModel;
import Door.Access.Util.UInt32Util;
import java.util.Calendar;

/**
 *
 * @author 英泽电子
 */
public class Door89HCommandWatchResponse extends AbstractWatchResponse {

    public Door89HCommandWatchResponse() {
        super(new Door8800Decompile());
    }

    @Override
    protected void fireWatchEvent(ConnectorDetail connectorDetail, INConnectorEvent oEvent, INPacketModel model) {
        Door8800PacketModel packet = (Door8800PacketModel) model;
        if (packet.GetCode() == UInt32Util.UINT32_MAX) {
            if (packet.GetCmdType() == 0x19) {
                Door8800WatchTransaction watchevent = new Door8800WatchTransaction();
                watchevent.CmdType = packet.GetCmdType();
                watchevent.CmdIndex = packet.GetCmdIndex();
                watchevent.CmdPar = packet.GetCmdPar();
                watchevent.SN = packet.GetSN();
                DefinedTransaction dt;
                switch (watchevent.CmdIndex) {
                    case 1://读卡信息
                        CardTransaction card = new CardTransaction();
                        card.SetBytes(packet.GetDatabuff());

                        watchevent.EventData = card;
                        break;
                    case 2://出门开关信息
                        ButtonTransaction ButtonTrn = new ButtonTransaction();
                        ButtonTrn.SetBytes(packet.GetDatabuff());

                        watchevent.EventData = ButtonTrn;
                        break;
                    case 3://门磁信息
                        DoorSensorTransaction DoorSensorTrn = new DoorSensorTransaction();
                        DoorSensorTrn.SetBytes(packet.GetDatabuff());

                        watchevent.EventData = DoorSensorTrn;
                        break;
                    case 4://远程开门信息
                        SoftwareTransaction SoftwareTrn = new SoftwareTransaction();
                        SoftwareTrn.SetBytes(packet.GetDatabuff());

                        watchevent.EventData = SoftwareTrn;
                        break;
                    case 5://报警信息
                        AlarmTransaction AlarmTrn = new AlarmTransaction();
                        AlarmTrn.SetBytes(packet.GetDatabuff());

                        watchevent.EventData = AlarmTrn;
                        break;
                    case 6://系统信息
                        SystemTransaction SystemTrn = new SystemTransaction();
                        SystemTrn.SetBytes(packet.GetDatabuff());

                        watchevent.EventData = SystemTrn;
                        break;
                    case 0x22://连接保活包
                        KeepAliveTransaction kp = new KeepAliveTransaction();
                        watchevent.EventData = kp;
                        break;
                    default:
                        dt = new DefinedTransaction(watchevent.CmdIndex, 0, Calendar.getInstance());
                        if (packet.GetDataLen() > 0) {
                            dt.SetWatchData(packet.GetDatabuff());
                        }
                        watchevent.EventData = dt;
                }
                oEvent.WatchEvent(connectorDetail, watchevent);
            }
        }
    }
}

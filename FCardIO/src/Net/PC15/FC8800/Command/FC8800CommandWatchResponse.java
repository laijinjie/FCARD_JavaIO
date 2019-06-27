/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Net.PC15.FC8800.Command;

import Net.PC15.Command.AbstractWatchResponse;
import Net.PC15.Connector.ConnectorDetail;
import Net.PC15.Connector.INConnectorEvent;
import Net.PC15.FC8800.Command.Data.AlarmTransaction;
import Net.PC15.FC8800.Command.Data.ButtonTransaction;
import Net.PC15.FC8800.Command.Data.CardTransaction;
import Net.PC15.FC8800.Command.Data.DefinedTransaction;
import Net.PC15.FC8800.Command.Data.DoorSensorTransaction;
import Net.PC15.FC8800.Command.Data.FC8800WatchTransaction;
import Net.PC15.FC8800.Command.Data.SoftwareTransaction;
import Net.PC15.FC8800.Command.Data.SystemTransaction;
import Net.PC15.FC8800.Packet.FC8800Decompile;
import Net.PC15.FC8800.Packet.FC8800PacketModel;
import Net.PC15.Packet.INPacketModel;
import Net.PC15.Util.UInt32Util;
import io.netty.buffer.ByteBuf;
import java.util.Calendar;

/**
 *
 * @author 赖金杰
 */
public class FC8800CommandWatchResponse extends AbstractWatchResponse {

    public FC8800CommandWatchResponse() {
        super(new FC8800Decompile());
    }

    @Override
    protected void fireWatchEvent(ConnectorDetail connectorDetail, INConnectorEvent oEvent, INPacketModel model) {
        FC8800PacketModel packet = (FC8800PacketModel) model;
        if (packet.GetCode() == UInt32Util.UINT32_MAX) {
            if (packet.GetCmdType() == 0x19) {
                FC8800WatchTransaction watchevent = new FC8800WatchTransaction();
                watchevent.CmdType = packet.GetCmdType();
                watchevent.CmdIndex = packet.GetCmdIndex();
                watchevent.CmdPar = packet.GetCmdPar();
                watchevent.SN = packet.GetSN();
                DefinedTransaction dt;
                switch (watchevent.CmdIndex) {
                    case 1://读卡信息
                        //if ((buf.capacity() - 4) % 21 == 0) {
                        ByteBuf buf = packet.GetDatabuff();
                        //fc89H
                        if (buf.capacity()  == 17) {
                            Net.PC15.FC89H.Command.Data.CardTransaction card = new Net.PC15.FC89H.Command.Data.CardTransaction();
                            card.SetBytes(buf);
                            watchevent.EventData = card;
                        }
                        else {
                            CardTransaction card = new CardTransaction();
                            card.SetBytes(buf);
                            watchevent.EventData = card;
                        }
                        
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
                    default:
                        dt = new DefinedTransaction(watchevent.CmdIndex, 0, Calendar.getInstance());
                        if(packet.GetDataLen()>0)
                        {
                            dt.SetWatchData(packet.GetDatabuff());
                        }                        
                        watchevent.EventData = dt;
                }
                oEvent.WatchEvent(connectorDetail, watchevent);
            }
        }
    }

}

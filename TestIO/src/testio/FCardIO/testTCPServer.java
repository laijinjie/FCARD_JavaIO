/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package testio.FCardIO;

import Door.Access.Command.CommandDetail;
import Door.Access.Command.CommandParameter;
import Door.Access.Command.INCommand;
import Door.Access.Command.INCommandResult;
import Door.Access.Connector.ConnectorAllocator;
import Door.Access.Connector.ConnectorDetail;
import Door.Access.Connector.E_ControllerType;
import Door.Access.Connector.INConnectorEvent;
import Door.Access.Connector.TCPClient.TCPClientDetail;
import Door.Access.Connector.TCPServer.TCPServerClientDetail;
import Door.Access.Data.AbstractTransaction;
import Door.Access.Data.BytesData;
import Door.Access.Data.INData;
import Door.Access.Door8800.Command.Data.AlarmTransaction;
import Door.Access.Door8800.Command.Data.ButtonTransaction;
import Door.Access.Door8800.Command.Data.CardTransaction;
import Door.Access.Door8800.Command.Data.DefinedTransaction;
import Door.Access.Door8800.Command.Data.DoorSensorTransaction;
import Door.Access.Door8800.Command.Data.Door8800WatchTransaction;
import Door.Access.Door8800.Command.Data.SoftwareTransaction;
import Door.Access.Door8800.Command.Data.SystemTransaction;
import Door.Access.Door8800.Command.Door.OpenDoor;
import Door.Access.Door8800.Command.Door.Parameter.OpenDoor_Parameter;
import Door.Access.Door8800.Command.Door.ReadRelayOption;
import Door.Access.Door8800.Command.System.ReadWorkStatus;
import Door.Access.Door8800.Door8800Identity;
import Door.Access.Door8800.Packet.Door8800Decompile;
import Door.Access.Door8800.Packet.Door8800PacketModel;
import Door.Access.Packet.INPacketModel;
import Door.Access.Packet.PacketDecompileAllocator;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import java.util.ArrayList;

/**
 *
 * @author Administrator
 */
public class testTCPServer implements INConnectorEvent{

    private ConnectorAllocator _Allocator;

    public testTCPServer() {
        _Allocator = ConnectorAllocator.GetAllocator();
        _Allocator.AddListener(this);
        //_Allocator.Listen("192.168.1.30", 9999);//??????????????????IP??????????????????????????????
        _Allocator.Listen( 9999);//????????????
        
    }

    @Override
    public void ClientOnline(ConnectorDetail client) {
        //??????????????????client???????????????????????????
        /*
        //????????????????????????
        CommandDetail detial = new CommandDetail();
        detial.Connector = client;
        detial.Identity = new Door8800Identity("FC-8940A46060007", "FFFFFFFF", E_ControllerType.Door8900);
        OpenDoor_Parameter par = new OpenDoor_Parameter(detial);
        par.Door.SetDoor(2, 1);
        par.Door.SetDoor(3, 1);
        INCommand cmd = new OpenDoor(par);
        _Allocator.AddCommand(cmd);
        */
        
//        System.out.println("?????????????????????" + client.Remote.toString() + "????????????ID???" + client.ClientID);
    }

    @Override
    public void ClientOffline(ConnectorDetail client) {
//        System.out.println("?????????????????????" + client.Remote.toString() + "????????????ID???" + client.ClientID);
    }
    @Override
    public void WatchEvent(ConnectorDetail detial, INData event) {
        //??????????????????????????????
        if (event instanceof BytesData) {
            BytesData b = (BytesData) event;
            TCPServerClientDetail cd = (TCPServerClientDetail) detial;

            ByteBuf dBuf = b.GetBytes();

            //???????????????Door8800???Door8900?????????
            if (dBuf.getByte(0) == 0x7e) {
                Door8800Decompile decompile = new Door8800Decompile();

                ArrayList<INPacketModel> oRetPack = new ArrayList<>(10);
                if (decompile.Decompile(dBuf, oRetPack)) {
                    //????????????????????????????????? ????????????????????????
                    _Allocator.AddWatchDecompile(cd, PacketDecompileAllocator.GetDecompile(E_ControllerType.Door8900));
                    Door8800PacketModel m = (Door8800PacketModel) oRetPack.get(0);
                    // m.GetSN() -- ???????????????????????????SN??????
                    System.out.println("?????????ID:" + cd.ClientID + "(" + m.GetSN() + ")?????????????????????" + ByteBufUtil.hexDump(dBuf));
                    //?????????????????????????????????????????????????????????????????? BeginWatch
                    /*
                        CommandDetail detial = new CommandDetail();
                        detial.Connector = client;
                        detial.Identity = new Door8800Identity(m.GetSN(), "FFFFFFFF", E_ControllerType.Door8900);
                        CommandParameter par = new CommandParameter(detial);
                        INCommand cmd = new BeginWatch(par);
                        _Allocator.AddCommand(cmd);
                     */
                }

            } else {
                
            }
        } else {
            //???????????????????????????
            if (event instanceof Door8800WatchTransaction) {
                Door8800WatchTransaction watchevent = (Door8800WatchTransaction) event;
                AbstractTransaction tr = (AbstractTransaction) watchevent.EventData;
                System.out.println("?????????????????????" + tr.getClass().toString());
                switch (watchevent.CmdIndex) {
                    case 1://????????????
                        CardTransaction card = (CardTransaction) watchevent.EventData;

                        break;
                    case 2://??????????????????
                        ButtonTransaction ButtonTrn = (ButtonTransaction) watchevent.EventData;

                        break;
                    case 3://????????????
                        DoorSensorTransaction DoorSensorTrn = (DoorSensorTransaction) watchevent.EventData;

                        break;
                    case 4://??????????????????
                        SoftwareTransaction SoftwareTrn = (SoftwareTransaction) watchevent.EventData;

                        break;
                    case 5://????????????
                        AlarmTransaction AlarmTrn = (AlarmTransaction) watchevent.EventData;

                        break;
                    case 6://????????????
                        SystemTransaction SystemTrn = (SystemTransaction) watchevent.EventData;

                        break;
                    default:
                        DefinedTransaction dt = (DefinedTransaction) watchevent.EventData;

                }
            } else {
                System.out.println("testio.FCardIO.FCardIOTest.WatchEvent() -- ????????????");
            }
        }

    }
    
    @Override
    public void CommandCompleteEvent(INCommand cmd, INCommandResult result) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void CommandProcessEvent(INCommand cmd) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void ConnectorErrorEvent(INCommand cmd, boolean isStop) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void ConnectorErrorEvent(ConnectorDetail detial) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void CommandTimeout(INCommand cmd) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void PasswordErrorEvent(INCommand cmd) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void ChecksumErrorEvent(INCommand cmd) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }



}

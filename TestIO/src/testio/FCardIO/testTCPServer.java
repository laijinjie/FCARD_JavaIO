/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package testio.FCardIO;

import Net.PC15.Command.CommandDetial;
import Net.PC15.Command.CommandParameter;
import Net.PC15.Command.INCommand;
import Net.PC15.Command.INCommandResult;
import Net.PC15.Connector.ConnectorAllocator;
import Net.PC15.Connector.ConnectorDetial;
import Net.PC15.Connector.E_ControllerType;
import Net.PC15.Connector.INConnectorEvent;
import Net.PC15.Connector.TCPClient.TCPClientDetial;
import Net.PC15.Connector.TCPServer.TCPServerClientDetial;
import Net.PC15.Data.AbstractTransaction;
import Net.PC15.Data.BytesData;
import Net.PC15.Data.INData;
import Net.PC15.FC8800.Command.Data.AlarmTransaction;
import Net.PC15.FC8800.Command.Data.ButtonTransaction;
import Net.PC15.FC8800.Command.Data.CardTransaction;
import Net.PC15.FC8800.Command.Data.DefinedTransaction;
import Net.PC15.FC8800.Command.Data.DoorSensorTransaction;
import Net.PC15.FC8800.Command.Data.FC8800WatchTransaction;
import Net.PC15.FC8800.Command.Data.SoftwareTransaction;
import Net.PC15.FC8800.Command.Data.SystemTransaction;
import Net.PC15.FC8800.Command.Door.OpenDoor;
import Net.PC15.FC8800.Command.Door.Parameter.OpenDoor_Parameter;
import Net.PC15.FC8800.Command.Door.ReadRelayOption;
import Net.PC15.FC8800.Command.System.ReadWorkStatus;
import Net.PC15.FC8800.FC8800Identity;
import Net.PC15.FC8800.Packet.FC8800Decompile;
import Net.PC15.FC8800.Packet.FC8800PacketModel;
import Net.PC15.Packet.INPacketModel;
import Net.PC15.Packet.PacketDecompileAllocator;
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
        //_Allocator.Listen("192.168.1.30", 9999);//这个可以指定IP监听，多网卡时使用。
        _Allocator.Listen( 9999);//开始监听
        
    }

    @Override
    public void ClientOnline(TCPServerClientDetial client) {
        //这里可以保存client对象，以便后续操作
        /*
        //示例调用远程开门
        CommandDetial detial = new CommandDetial();
        detial.Connector = client;
        detial.Identity = new FC8800Identity("FC-8940A46060007", "FFFFFFFF", E_ControllerType.FC8900);
        OpenDoor_Parameter par = new OpenDoor_Parameter(detial);
        par.Door.SetDoor(2, 1);
        par.Door.SetDoor(3, 1);
        INCommand cmd = new OpenDoor(par);
        _Allocator.AddCommand(cmd);
        */
        
        System.out.println("有客户端上线：" + client.Remote.toString() + "，客户端ID：" + client.ClientID);
    }

    @Override
    public void ClientOffline(TCPServerClientDetial client) {
        System.out.println("有客户端离线：" + client.Remote.toString() + "，客户端ID：" + client.ClientID);
    }
    @Override
    public void WatchEvent(ConnectorDetial detial, INData event) {
        //在这里做一次身份验证
        if (event instanceof BytesData) {
            BytesData b = (BytesData) event;
            TCPServerClientDetial cd = (TCPServerClientDetial) detial;

            ByteBuf dBuf = b.GetBytes();

            //检查是否为FC8800、FC8900数据包
            if (dBuf.getByte(0) == 0x7e) {
                FC8800Decompile decompile = new FC8800Decompile();

                ArrayList<INPacketModel> oRetPack = new ArrayList<>(10);
                if (decompile.Decompile(dBuf, oRetPack)) {
                    //给客户端通道添加解析器 以便收到监控消息
                    _Allocator.AddWatchDecompile(cd, PacketDecompileAllocator.GetDecompile(E_ControllerType.FC8900));
                    FC8800PacketModel m = (FC8800PacketModel) oRetPack.get(0);
                    // m.GetSN() -- 这个就是此控制器的SN号，
                    System.out.println("客户端ID:" + cd.ClientID + "(" + m.GetSN() + ")，收到数据包：" + ByteBufUtil.hexDump(dBuf));
                    //这时如果刷卡接收不到监控消息，还需要发送命令 BeginWatch
                    /*
                        CommandDetial detial = new CommandDetial();
                        detial.Connector = client;
                        detial.Identity = new FC8800Identity(m.GetSN(), "FFFFFFFF", E_ControllerType.FC8900);
                        CommandParameter par = new CommandParameter(detial);
                        INCommand cmd = new BeginWatch(par);
                        _Allocator.AddCommand(cmd);
                     */
                }

            } else {
                
            }
        } else {
            //控制板监控消息处理
            if (event instanceof FC8800WatchTransaction) {
                FC8800WatchTransaction watchevent = (FC8800WatchTransaction) event;
                AbstractTransaction tr = (AbstractTransaction) watchevent.EventData;
                System.out.println("收到监控事件：" + tr.getClass().toString());
                switch (watchevent.CmdIndex) {
                    case 1://读卡信息
                        CardTransaction card = (CardTransaction) watchevent.EventData;

                        break;
                    case 2://出门开关信息
                        ButtonTransaction ButtonTrn = (ButtonTransaction) watchevent.EventData;

                        break;
                    case 3://门磁信息
                        DoorSensorTransaction DoorSensorTrn = (DoorSensorTransaction) watchevent.EventData;

                        break;
                    case 4://远程开门信息
                        SoftwareTransaction SoftwareTrn = (SoftwareTransaction) watchevent.EventData;

                        break;
                    case 5://报警信息
                        AlarmTransaction AlarmTrn = (AlarmTransaction) watchevent.EventData;

                        break;
                    case 6://系统信息
                        SystemTransaction SystemTrn = (SystemTransaction) watchevent.EventData;

                        break;
                    default:
                        DefinedTransaction dt = (DefinedTransaction) watchevent.EventData;

                }
            } else {
                System.out.println("testio.FCardIO.FCardIOTest.WatchEvent() -- 未知消息");
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
    public void ConnectorErrorEvent(ConnectorDetial detial) {
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

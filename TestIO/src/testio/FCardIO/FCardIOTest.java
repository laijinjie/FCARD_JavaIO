/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package testio.FCardIO;

import Net.PC15.FC8800.Command.Door.Parameter.OpenDoor_Parameter;
import Net.PC15.Command.CommandDetail;
import Net.PC15.Command.CommandParameter;
import java.util.concurrent.Semaphore;
import Net.PC15.Connector.ConnectorAllocator;
import Net.PC15.FC8800.Command.FC8800Command;
import Net.PC15.Command.INCommand;
import Net.PC15.Command.INCommandResult;
import Net.PC15.Connector.ConnectorDetail;
import Net.PC15.Connector.E_ControllerType;
import Net.PC15.Connector.INConnectorEvent;
import Net.PC15.Connector.TCPClient.TCPClientDetail;
import Net.PC15.Connector.TCPServer.IPEndPoint;
import Net.PC15.Connector.TCPServer.TCPServerClientDetail;
import Net.PC15.Data.AbstractTransaction;
import Net.PC15.Data.BytesData;
import Net.PC15.Data.INData;
import Net.PC15.FC8800.Command.Card.Parameter.*;
import Net.PC15.FC8800.Command.Card.*;
import Net.PC15.FC8800.Command.Card.Result.*;
import Net.PC15.FC8800.Command.Data.AlarmTransaction;
import Net.PC15.FC8800.Command.Data.ButtonTransaction;
import Net.PC15.FC8800.Command.Data.CardDetail;
import Net.PC15.FC8800.Command.Data.CardTransaction;
import Net.PC15.FC8800.Command.Data.DefinedTransaction;
import Net.PC15.FC8800.Command.Data.DoorSensorTransaction;
import Net.PC15.FC8800.Command.Data.FC8800WatchTransaction;
import Net.PC15.FC8800.Command.Data.SoftwareTransaction;
import Net.PC15.FC8800.Command.Data.SystemTransaction;
import Net.PC15.FC8800.Command.Door.OpenDoor;
import Net.PC15.FC8800.Command.Door.ReadRelayOption;
import Net.PC15.FC8800.Command.FC8800CommandWatchResponse;
import Net.PC15.FC8800.Command.System.ReadWorkStatus;
import Net.PC15.FC8800.FC8800Identity;
import Net.PC15.FC8800.Packet.FC8800Decompile;
import Net.PC15.FC8800.Packet.FC8800PacketModel;
import Net.PC15.Packet.INPacketModel;
import Net.PC15.Packet.PacketDecompileAllocator;
import Net.PC15.Util.ByteUtil;
import Net.PC15.Util.StringUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *
 * @author 赖金杰
 */
public class FCardIOTest implements INConnectorEvent {

    private final Semaphore available = new Semaphore(0, true);
    private ConnectorAllocator _Allocator;
    private ExecutorService workService;

    public FCardIOTest() {
        _Allocator = ConnectorAllocator.GetAllocator();
        _Allocator.AddListener(this);
        _Allocator.Listen("192.168.1.30", 9999);
        //_Allocator.Listen(988); //第二种监听也可以用

        ArrayList<IPEndPoint> servers = _Allocator.GetTCPServerList();
        for (IPEndPoint server : servers) {
            System.out.println("正在监听的服务端口：" + server.toString());
        }
        workService = Executors.newFixedThreadPool(5);

    }

    public void syn() {
        try {
            available.acquire();
        } catch (Exception e) {
        }

    }

    public void release() {
        _Allocator.Release();
        available.release();
    }

    public void TestCommand() {
        OpenDoor_Parameter par = new OpenDoor_Parameter(getCommandDetail());
        par.Door.SetDoor(2, 1);
        par.Door.SetDoor(3, 1);
        INCommand cmd = new OpenDoor(par);
        _Allocator.AddCommand(cmd);

    }

    public void TestReadAllCard() {
        Net.PC15.Command.CommandDetail detial = getCommandDetail();
        detial.Timeout = 1000;
        ReadCardDataBase_Parameter par = new ReadCardDataBase_Parameter(detial, 1);
        INCommand cmd = new ReadCardDataBase(par);

        /*
        cmd.SendCommand(this);
        ByteBuf bData = ByteUtil.ALLOCATOR.buffer(600);
        bData.writeBytes(StringUtil.HexToByte("7e0e6cc69d46432d38393430413436303630303037ffffffff370100000000100001d4c0000003e900008ca000000004357e"));
        cmd.CheckResponse(this, bData);
        bData.clear();
        bData.writeBytes(StringUtil.HexToByte("7e98fa79a046432d38393430413436303630303037ffffffff3703000000014e0000000a00001025ff97206847171115000001010101fffff800ffffffff000000000000000000106b9084375159171115000001010101fffff800ffffffff00000000000000000010739654004362171115000001010101fffff800ffffffff00000000000000000010925b74577437171115000001010101fffff800ffffffff00000000000000000010b92741172674171115000001010101fffff800ffffffff000000000000000000111d7d72521104171115000001010101fffff800ffffffff000000000000000000116b1429318619171115000001010101fffff800ffffffff0000000000000000001189da49891694171115000001010101fffff800ffffffff00000000000000000011a89f46857728171115000001010101fffff800ffffffff000000000000000000123bc714430597171115000001010101fffff800ffffffff00000000000000d27e"));
        cmd.CheckResponse(this, bData);*/
        _Allocator.AddCommand(cmd);
    }

    private Net.PC15.Command.CommandDetail getCommandDetail() {
        Net.PC15.Command.CommandDetail detial = new Net.PC15.Command.CommandDetail();
        detial.Connector = new TCPClientDetail("192.168.1.169", 8000);
        detial.Identity = new FC8800Identity("FC-8940A46060007", "FFFFFFFF", E_ControllerType.FC8900);
        return detial;
    }

    public static void main(String[] args) {
        FCardIOTest test = new FCardIOTest();

        test.TestReadAllCard();
        test.syn();
    }

    @Override
    public void CommandCompleteEvent(INCommand cmd, INCommandResult result) {
        if (cmd instanceof ReadCardDataBase) {
            ReadCardDataBase_Result r = (ReadCardDataBase_Result) cmd.getCommandResult();
            if (r.DataBaseSize > 0) {
                System.out.println("读卡片数据成功，读取数量：" + r.CardList.size());
                int len = r.CardList.size();

                Collections.sort(r.CardList);
                ArrayList<CardDetail> CardList = r.CardList;
                int iSearch = CardDetail.SearchCardDetail(CardList, "123");

                for (int i = 0; i < len; i++) {
                    CardDetail cd = CardList.get(i);
                    iSearch = CardDetail.SearchCardDetail(CardList, cd.GetCardData());
                    if (iSearch != i) {

                    }
                }
            } else {
                System.out.println("读卡片数据成功，读取数量：" + r.DataBaseSize);
            }

        } else {
            System.out.println("CommandCompleteEvent");
        }

    }

    @Override
    public void CommandProcessEvent(INCommand cmd) {
        System.out.println("命令进度更新：" + cmd.getProcessMax() + "/" + cmd.getProcessStep());
    }

    @Override
    public void ConnectorErrorEvent(INCommand cmd, boolean isStop) {
        System.out.println("ConnectorErrorEvent");
    }

    @Override
    public void ConnectorErrorEvent(ConnectorDetail detial) {
        System.out.println("ConnectorErrorEvent");
    }

    @Override
    public void CommandTimeout(INCommand cmd) {
        System.out.println("CommandTimeout");
    }

    @Override
    public void PasswordErrorEvent(INCommand cmd) {
        System.out.println("PasswordErrorEvent");
    }

    @Override
    public void ChecksumErrorEvent(INCommand cmd) {
        System.out.println("ChecksumErrorEvent");
    }

    @Override
    public void WatchEvent(ConnectorDetail detial, INData event) {
        if (event instanceof BytesData) {
            BytesData b = (BytesData) event;
            TCPServerClientDetail cd = (TCPServerClientDetail) detial;

            ByteBuf dBuf = b.GetBytes();

            //检查是否为FC8800、FC8900数据包
            if (dBuf.getByte(0) == 0x7e) {
                FC8800Decompile decompile = new FC8800Decompile();

                ArrayList<INPacketModel> oRetPack = new ArrayList<>(10);
                if (decompile.Decompile(dBuf, oRetPack)) {
                    //给客户端通道添加解析器
                    _Allocator.AddWatchDecompile(cd, PacketDecompileAllocator.GetDecompile(E_ControllerType.FC8900));
                    FC8800PacketModel m = (FC8800PacketModel) oRetPack.get(0);

                    System.out.println("客户端ID:" + cd.ClientID + "(" + m.GetSN() + ")，收到数据包：" + ByteBufUtil.hexDump(dBuf));
                }

            } else {
                //System.out.println("客户端ID:" + cd.ClientID + "，收到数据包：" + ByteBufUtil.hexDump( dBuf));
            }
        } else {
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
    public void ClientOnline(TCPServerClientDetail client) {
        System.out.println("有客户端上线：" + client.Remote.toString() + "，客户端ID：" + client.ClientID);
        workService.submit(() -> {
            try {
                CommandDetail detial = getCommandDetail();
                detial.Connector = client;
                CommandParameter par = new CommandParameter(detial);

                INCommand cmd = new ReadRelayOption(par);
                _Allocator.AddCommand(cmd);

                cmd = new ReadWorkStatus(par);
                _Allocator.AddCommand(cmd);

                Thread.sleep(30000);//设定30秒后自动短线
                _Allocator.CloseConnector(client);
            } catch (Exception e) {
            }

        });

    }

    @Override
    public void ClientOffline(TCPServerClientDetail client) {
        System.out.println("有客户端离线：" + client.Remote.toString() + "，客户端ID：" + client.ClientID);
    }
}

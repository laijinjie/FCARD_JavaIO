/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package demoapp;

import Door.Access.Command.CommandDetail;
import Door.Access.Command.CommandParameter;
import Door.Access.Command.INCommand;
import Door.Access.Command.INCommandResult;
import Door.Access.Command.INWatchResponse;
import Door.Access.Connector.ConnectorAllocator;
import Door.Access.Connector.ConnectorDetail;
import Door.Access.Connector.E_ControllerType;
import Door.Access.Connector.INConnector;
import Door.Access.Connector.INConnectorEvent;
import Door.Access.Connector.TCPServer.TCPServerAllocator;
import Door.Access.Connector.TCPServer.TCPServerClientDetail;
import Door.Access.Data.INData;
import Door.Access.Door8800.Command.Data.Door8800WatchTransaction;
import Door.Access.Door8800.Command.System.Result.SearchEquptOnNetNum_Result;
import Door.Access.Door8800.Command.System.SearchEquptOnNetNum;
import Door.Access.Door8800.Door8800Identity;
import Door.Access.Packet.PacketDecompileAllocator;
import Door.Access.Util.StringUtil;
import Door.Access.Util.TimeUtil;
import Face.AdditionalData.Parameter.ReadFeatureCode_Parameter;
import Face.AdditionalData.Parameter.ReadFile_Parameter;
import Face.AdditionalData.Result.ReadFeatureCode_Result;
import Face.AdditionalData.Result.ReadFile_Result;
import Face.Data.ConnectTestTransaction;
import io.netty.buffer.ByteBuf;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author kaifa
 */
public class TCPServerMonitorByFace implements INConnectorEvent {



    private ConnectorAllocator _Allocator;
    private String _LocalIP = null;
    private int _LocalPort = 8686;
    protected ConcurrentHashMap<String, ConnectContext> _ConnectorMap;

    protected ConcurrentHashMap<String, ConnectContext> _SNMap;

    public TCPServerMonitorByFace() {
        _Allocator = ConnectorAllocator.GetAllocator();
        _Allocator.AddListener(this);
        _ConnectorMap = new ConcurrentHashMap<String, ConnectContext>(1024);
        _SNMap = new ConcurrentHashMap<String, ConnectContext>(1024);
    }

    private void InputTestPar() {
        System.out.println("????????????????????????TCP????????????");
        java.util.Scanner sc = new java.util.Scanner(System.in);
        System.out.println("????????????????????????IP???");
        _LocalIP = sc.nextLine();//???????????????
        System.out.println("????????????????????????????????????");
        _LocalPort = sc.nextInt();

        sc.close();
    }

    public void BeginMonitor() {
        InputTestPar();

        //???????????????????????????????????? keepalive ??????
        //??????????????????????????????????????????????????????????????????keepalive?????????????????????????????????
        //????????????????????????
        TCPServerAllocator.IdleStateTime_Second = 90;
        //?????????keepalive?????????????????????
        TCPServerAllocator.KeepAliveMsg = StringUtil.HexToByte("7E30303030303030303030303030303030FFFFFFFF630B6AAC01020000000000837E");

        _Allocator.Listen(_LocalIP, _LocalPort);
        printLog("????????????TCP?????????????????????" + _LocalIP + ":" + _LocalPort);

    }

    @Override
    public void CommandCompleteEvent(INCommand cmd, INCommandResult result) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        printLog("CommandCompleteEvent -- " + cmd.getClass().getName());
        if (cmd instanceof Face.AdditionalData.ReadFile) {
            ReadFile_Result fileResult = (ReadFile_Result) result;
            //???????????????????????????
            printLog("????????????????????????????????????" + fileResult.Result + ",??????????????? " + fileResult.FileSize + "???CRC32???" + fileResult.FileCRC);
            //?????????????????????
            try {
                FileOutputStream fileout = new FileOutputStream("test.jpg");
                fileout.write(fileResult.FileDatas);
            } catch (FileNotFoundException fex) {
                printLog("??????????????????????????????????????????????????????1 " + fex.getMessage());
            } catch (IOException ioex) {
                printLog("??????????????????????????????????????????????????????2 " + ioex.getMessage());
            }
        }
    }

    @Override
    public void CommandProcessEvent(INCommand cmd) {
        //printLog("CommandProcessEvent:" + cmd.getClass().getName());
    }

    @Override
    public void ConnectorErrorEvent(INCommand cmd, boolean isStop) {
        printLog("ConnectorErrorEvent  INCommand -- " + cmd.getClass().getName());
    }

    @Override
    public void ConnectorErrorEvent(ConnectorDetail detail) {
        printLog("ConnectorErrorEvent  ConnectorDetail");
    }

    @Override
    public void CommandTimeout(INCommand cmd) {
        printLog("CommandTimeout  -- " + cmd.getClass().getName());
    }

    @Override
    public void PasswordErrorEvent(INCommand cmd) {
        printLog("PasswordErrorEvent  -- " + cmd.getClass().getName());
    }

    @Override
    public void ChecksumErrorEvent(INCommand cmd) {
        printLog("ChecksumErrorEvent  -- " + cmd.getClass().getName());
    }

    @Override
    public void WatchEvent(ConnectorDetail detail, INData event) {
        TCPServerClientDetail tcpclientDTL = (TCPServerClientDetail) detail;
        CommandDetail cmdDTL;
        String key = ConnectContext.GetConnectKey(tcpclientDTL.ClientID);
        if (_ConnectorMap.containsKey(key)) {
            ConnectContext ct = _ConnectorMap.get(key);

            if (event instanceof Door8800WatchTransaction) {
                Door8800WatchTransaction watchEvent = (Door8800WatchTransaction) event;
                if (ct.SN == null) {
                    ct.SN = watchEvent.SN;
                    AddSN(ct);

                    BeginOpenWatch(ct.SN);
                }

                if (watchEvent.EventData instanceof Face.Data.CardTransaction) {
                    Face.Data.CardTransaction faceRecord = (Face.Data.CardTransaction) watchEvent.EventData;
                    BeginReadFile(ct.SN, faceRecord);
                }

                //?????????????????????????????????????????????
                if (watchEvent.EventData instanceof Face.Data.ConnectTestTransaction) {
                    //????????????????????????
                    SendConnectTestResult(ct.SN);
                }
            }
            printLog("WatchEvent " + event.toString());
        }

    }

    private void SendConnectTestResult(String SN) {
        CommandDetail cmdDTL;
        ConnectContext ct = GetConnectContext(SN);
        if (ct == null) {
            return;
        }
        cmdDTL = GetCommandDetail(ct);
        Face.System.SendConnectTestResponse cmdSendResult = new Face.System.SendConnectTestResponse(new CommandParameter(cmdDTL));
        ct.Connector.AddCommand(cmdSendResult);
    }

    /**
     * ?????????????????????????????????
     *
     * @param SN
     */
    private void BeginOpenWatch(String SN) {
        CommandDetail cmdDTL;
        ConnectContext ct = GetConnectContext(SN);
        if (ct == null) {
            return;
        }

        if (ct.SN != null && ct.IsOpenWatch == false) {
            //????????????????????????
            cmdDTL = GetCommandDetail(ct);
            Face.System.BeginWatch cmdBeginWatch = new Face.System.BeginWatch(new CommandParameter(cmdDTL));
            ct.Connector.AddCommand(cmdBeginWatch);
            ct.IsOpenWatch = true;
            //??????????????????
            Face.System.WriteOfflineRecordPush cmdPush = new Face.System.WriteOfflineRecordPush(new Face.System.Parameter.WriteOfflineRecordPush_Parameter(cmdDTL, true));
            ct.Connector.AddCommand(cmdPush);
        }
    }

    /**
     * ??????????????????????????????
     *
     * @param SN
     * @param record
     */
    private void BeginReadFile(String SN, Face.Data.CardTransaction faceRecord) {
        CommandDetail cmdDTL;
        ConnectContext ct = GetConnectContext(SN);
        if (ct == null) {
            return;
        }

        if (faceRecord.getPhoto() == 1) {
            //??????????????????????????????
            cmdDTL = GetCommandDetail(ct);//??????????????????SN??????????????????
            if (cmdDTL == null) {
                return;//??????????????????????????????
            }
            Face.AdditionalData.ReadFile cmdReadFile
                    = new Face.AdditionalData.ReadFile(
                            new ReadFile_Parameter(cmdDTL, faceRecord.SerialNumber, 3, 1)
                    );

            ct.Connector.AddCommand(cmdReadFile);
        }
    }

    private CommandDetail GetCommandDetail(String SN) {
        CommandDetail result = null;

        ConnectContext ct = GetConnectContext(SN);
        if (ct != null) {
            result = GetCommandDetail(ct);
        }
        return result;
    }

    /**
     * ??????SN?????????????????????
     *
     * @param SN
     * @return
     */
    private ConnectContext GetConnectContext(String SN) {
        ConnectContext result = null;
        if (_SNMap.containsKey(SN)) {
            ConnectContext ct = _SNMap.get(SN);
            if (ct.IsOnline()) {
                result = ct;
            }
        }
        return result;
    }

    private CommandDetail GetCommandDetail(ConnectContext ct) {
        CommandDetail cmdDTL = new CommandDetail();
        cmdDTL.Connector = ct.ConnectorDTL;
        cmdDTL.Identity = new Door8800Identity(ct.SN, "FFFFFFFF", E_ControllerType.Face_Fingerprint);
        cmdDTL.Timeout = 3000;
        cmdDTL.RestartCount = 5;
        return cmdDTL;
    }

    @Override
    public void ClientOnline(ConnectorDetail clientDTL) {
        printLog("ClientOnline " + clientDTL.toString());
        TCPServerClientDetail tcpclientDTL = (TCPServerClientDetail) clientDTL;

        INConnector conn = _Allocator.GetConnector(clientDTL);

        ConnectContext ct = new ConnectContext(tcpclientDTL.ClientID, conn, clientDTL);

        conn.OpenForciblyConnect();
        conn.AddWatchDecompile(clientDTL, PacketDecompileAllocator.GetDecompile(E_ControllerType.Face_Fingerprint));

        //???????????? ????????????
        String key = "ClientID:" + tcpclientDTL.ClientID;
        _ConnectorMap.put(key, ct);
    }

    @Override
    public void ClientOffline(ConnectorDetail clientDTL) {
        printLog("ClientOffline " + clientDTL.toString());
        TCPServerClientDetail tcpclientDTL = (TCPServerClientDetail) clientDTL;
        String key = "ClientID:" + tcpclientDTL.ClientID;

        if (_ConnectorMap.containsKey(key)) {
            ConnectContext ct = _ConnectorMap.get(key);
            RemoveSN(ct);
            _ConnectorMap.remove(key);
        }
    }

    private void AddSN(ConnectContext ct) {
        String sKey = ct.SN;
        ConnectContext snCT;
        if (_SNMap.containsKey(ct.SN)) {
            snCT = _SNMap.get(sKey);
            snCT.SetNewContext(ct);
        } else {
            snCT = new ConnectContext(ct);
        }

        _SNMap.put(sKey, snCT);
    }

    private void RemoveSN(ConnectContext ct) {
        String sKey = ct.SN;
        ConnectContext snCT;
        if (_SNMap.containsKey(ct.SN)) {
            snCT = _SNMap.get(sKey);
            if (snCT.ClientID == ct.ClientID) {
                snCT.SetOffline();
            }
        }

    }

    private void printLog(String sLog) {
        Calendar now = Calendar.getInstance();
        String sNow = TimeUtil.FormatTime(now);
        System.out.println(sNow + " -- " + sLog);
    }
}

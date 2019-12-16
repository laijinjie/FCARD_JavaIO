/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Demo;
import Net.PC15.Command.CommandDetial;
import Net.PC15.Command.INCommand;
import Net.PC15.Command.INCommandResult;
import Net.PC15.Connector.*;
import Net.PC15.Connector.TCPClient.TCPClientDetial;
import Net.PC15.Connector.TCPServer.TCPServerClientDetial;
import Net.PC15.Data.INData;
import Net.PC15.FC8800.Command.Data.FC8800WatchTransaction;
import Net.PC15.FC8800.Command.Door.Parameter.OpenDoor_Parameter;
import Net.PC15.FC8800.Command.Door.Parameter.RemoteDoor_Parameter;
import Net.PC15.FC8800.Command.System.SearchEquptOnNetNum;
import Net.PC15.FC8800.FC8800Identity;
/**
 *
 * @author F
 */
public class OpenDoor  implements INConnectorEvent{
    private ConnectorAllocator _Allocator;
    private boolean mIsClose;

    public OpenDoor(ConnectorAllocator global) {
        if (global != null) {
            _Allocator = global;
        } else {
            System.out.println("命令对象不能为空");
            return;
        }
        //添加监听
        _Allocator.AddListener(this);
        //定义控制器连接信息
        CommandDetial commandDetial = new CommandDetial();
        TCPClientDetial tcpClientDetial = new TCPClientDetial("192.168.1.59", 8000);//IP地址，端口(默认8000)
        commandDetial.Connector = tcpClientDetial;
        commandDetial.Identity = new FC8800Identity("MC-5824T25070244", "FFFFFFFF", E_ControllerType.FC8800);//设置SN(16位字符)，密码(8位十六进制字符)，设备类型
        //定义命令参数
        OpenDoor_Parameter openDoor_parameter = new OpenDoor_Parameter(commandDetial);//初始化开门参数
        openDoor_parameter.Door.SetDoor(1, 1);//设定1号门执行操作
        openDoor_parameter.Door.SetDoor(2, 1);//设定2号门不执行操作
        Net.PC15.FC8800.Command.Door.OpenDoor openDoor = new Net.PC15.FC8800.Command.Door.OpenDoor(openDoor_parameter);
        //添加命令到队列
        _Allocator.AddCommand(openDoor);

    }

    public void CloseDoor() {
        CommandDetial commandDetial = new CommandDetial();
        TCPClientDetial tcpClientDetial = new TCPClientDetial("192.168.1.59", 8000);//IP地址，端口(默认8000)
        //定义控制器连接信息
        commandDetial.Connector = tcpClientDetial;
        commandDetial.Identity = new FC8800Identity("MC-5824T25070244", "FFFFFFFF", E_ControllerType.FC8800);//设置SN(16位字符)，密码(8位十六进制字符)，设备类型
        //定义命令参数
        RemoteDoor_Parameter par = new RemoteDoor_Parameter(commandDetial);//初始化开门参数
        par.Door.SetDoor(1, 1);//设定1号门执行操作
        par.Door.SetDoor(2, 1);//设定2号门不执行操作
        Net.PC15.FC8800.Command.Door.CloseDoor closeDoor = new Net.PC15.FC8800.Command.Door.CloseDoor(par);
        //添加命令到队列
        _Allocator.AddCommand(closeDoor);
    }

    public void ReleaseOD() {
        //删除监听
        _Allocator.DeleteListener(this);
        _Allocator = null;
    }
    

    @Override
    public void CommandCompleteEvent(INCommand cmd, INCommandResult result) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void CommandProcessEvent(INCommand cmd) {
        try {
            StringBuilder strBuf = new StringBuilder(100);
            strBuf.append("<html>");
            strBuf.append("当前命令：");
            strBuf.append("<br/>正在处理： ");
            strBuf.append(cmd.getProcessStep());
            strBuf.append(" / ");
            strBuf.append(cmd.getProcessMax());
            strBuf.append("</html>");
            strBuf = null;
        } catch (Exception e) {
            System.out.println("fcardiodemo.frmMain.CommandProcessEvent() -- 发生错误：" + e.toString());
        }

    }

    @Override
    public void ConnectorErrorEvent(INCommand cmd, boolean isStop) {
        try {
            StringBuilder strBuf = new StringBuilder(100);
            if (isStop) {
                strBuf.append("命令已手动停止!");
            } else {
                strBuf.append("网络连接失败!");
            }
            strBuf = null;
        } catch (Exception e) {
            System.out.println("fcardiodemo.frmMain.ConnectorErrorEvent() --- " + e.toString());
        }

    }

    @Override
    public void ConnectorErrorEvent(ConnectorDetial detial) {
        try {
            StringBuilder strBuf = new StringBuilder(100);
            strBuf.append("网络通道故障，IP信息：");
            strBuf = null;
        } catch (Exception e) {
            System.out.println("fcardiodemo.frmMain.ConnectorErrorEvent() -- " + e.toString());
        }

    }

    @Override
    public void CommandTimeout(INCommand cmd) {
        try {
            if (cmd instanceof SearchEquptOnNetNum) {
                return;
            }
            StringBuilder strBuf = new StringBuilder(100);
            strBuf.append("命令超时，已失败！");
            strBuf = null;
        } catch (Exception e) {
            System.out.println("fcardiodemo.frmMain.CommandTimeout() -- " + e.toString());
        }

    }

    @Override
    public void PasswordErrorEvent(INCommand cmd) {
        try {
            StringBuilder strBuf = new StringBuilder(100);
            strBuf.append("通讯密码错误，已失败！");
            strBuf = null;
        } catch (Exception e) {
            System.out.println("fcardiodemo.frmMain.PasswordErrorEvent() -- " + e.toString());
        }

    }

    @Override
    public void ChecksumErrorEvent(INCommand cmd) {
        try {
            StringBuilder strBuf = new StringBuilder(100);
            strBuf.append("命令返回的校验和错误，已失败！");
            strBuf = null;
        } catch (Exception e) {
            System.out.println("fcardiodemo.frmMain.ChecksumErrorEvent() -- " + e.toString());
        }

    }

    @Override
    public void WatchEvent(ConnectorDetial detial, INData event) {
        try {
            StringBuilder strBuf = new StringBuilder(100);
            strBuf.append("数据监控:");
            if (event instanceof FC8800WatchTransaction) {
                FC8800WatchTransaction WatchTransaction = (FC8800WatchTransaction) event;
                strBuf.append("，SN：");
                strBuf.append(WatchTransaction.SN);
                strBuf.append("\n");
            } else {
                strBuf.append("，未知事件：");
                strBuf.append(event.getClass().getName());
            }
            strBuf = null;
        } catch (Exception e) {
            System.out.println("fcardiodemo.frmMain.WatchEvent() -- " + e.toString());
        }
    }
    
    
    @Override
    public void ClientOnline(TCPServerClientDetial client) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void ClientOffline(TCPServerClientDetial client) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}

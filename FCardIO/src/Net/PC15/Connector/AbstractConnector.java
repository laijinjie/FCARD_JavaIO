/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Net.PC15.Connector;

import Net.PC15.Command.INCommand;
import Net.PC15.Command.INCommandRuntime;
import Net.PC15.Command.INIdentity;
import Net.PC15.Command.INWatchResponse;
import Net.PC15.Packet.PacketDecompileAllocator;
import io.netty.buffer.ByteBuf;
import java.util.Calendar;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * 抽象的连机器基类，定义了一些公共方法实现
 *
 * @author 赖金杰
 */
public abstract class AbstractConnector implements INConnector {

    protected ConcurrentLinkedQueue<INCommand> _CommandList;
    protected ConcurrentHashMap<String, INWatchResponse> _DecompileList;
    protected boolean _IsForcibly; //表示此连接是否一致保持连接
    protected INCommand _ActivityCommand;//正在活动中的命令
    protected E_ConnectorStatus _Status;
    protected INConnectorEvent _Event;
    protected Boolean _isRelease;
    protected Boolean _isInvalid;
    protected Calendar _ActivityDate;//上一次通道活动时间
    /**
     * 任务是否已加入队列或正在执行
     */
    protected boolean _IsRuning;

    public AbstractConnector() {
        _CommandList = new ConcurrentLinkedQueue<>();
        _DecompileList = new ConcurrentHashMap<>();
        _IsForcibly = false;
        _Status = E_ConnectorStatus.OnClosed;
        _isRelease = false;
        _isInvalid = false;
        _ActivityDate = Calendar.getInstance();
        _IsRuning=false;
    }

    @Override
    public boolean IsInvalid() {
        return _isInvalid;
    }

    /**
     * 更新通道活动时间
     */
    protected void UpdateActivityTime() {
        _ActivityDate = Calendar.getInstance();
    }

    /**
     * 检查通道是否已失效 1分钟无连接，无命令任务则自动失效
     */
    protected void CheckChanelIsInvalid() {
        long lConnectMillis = _ActivityDate.getTimeInMillis();
        long lNowMillis = Calendar.getInstance().getTimeInMillis();
        long lElapse = lNowMillis - lConnectMillis;//已经过事件
        long InvalidTime = 60 * 1000;//1分钟无连接，无命令任务则自动失效
        _isInvalid = (lElapse > InvalidTime);
    }

    @Override
    public int GetCommandCount() {
        return _CommandList.size();
    }

    @Override
    public synchronized void AddCommand(INCommand cmd) {
        _CommandList.offer(cmd);
        if (cmd.GetIdentity() != null) {
            AddWatchDecompile(PacketDecompileAllocator.GetDecompile(cmd.GetIdentity().GetIdentityType()));
        }

    }

    @Override
    public synchronized void AddWatchDecompile(INWatchResponse decompile) {
        if (decompile == null) {
            return;
        }
        //首先遍历检查是否已添加过此命令解析类
        String key = decompile.getClass().getCanonicalName();
        if (!_DecompileList.containsKey(key)) {
            _DecompileList.put(key, decompile);
        }
    }

    @Override
    public synchronized void Release() {
        if (_isRelease) {
            return;
        }
        _isRelease = true;
        Release0();
        _CommandList.clear();
        _CommandList = null;

        if (_DecompileList != null) {
            for (INWatchResponse value : _DecompileList.values()) {
                value.Release();
            }
            _DecompileList.clear();
            _DecompileList = null;
        }

        _ActivityCommand = null;
        _Status = null;
        _Event = null;

    }

    /**
     * 释放通道资源
     */
    protected abstract void Release0();

    @Override
    public boolean IsForciblyConnect() {
        return _IsForcibly;
    }

    @Override
    public void OpenForciblyConnect() {
        _IsForcibly = true;
    }

    @Override
    public void CloseForciblyConnect() {
        _IsForcibly = false;
    }

    @Override
    public void SetEventHandle(INConnectorEvent oEvent) {
        _Event = oEvent;
    }

    @Override
    public void run() {
        _IsRuning=true;
        
        CheckStatus();
        
        _IsRuning=false;
    }

    /**
     * 检查连接通道的状态，已确定下一步的操作
     */
    protected abstract void CheckStatus();

    @Override
    public E_ConnectorStatus GetStatus() {
        return _Status;
    }

    /**
     * 连接错误时，触发此回到函数
     *
     * @param oEvent
     */
    protected void RaiseConnectorErrorEvent(INCommand cmd, boolean bIsStopCommand) {
        if (!bIsStopCommand) {
            ConnectorDetail detail = GetConnectorDetail();

            if (_Event != null) {
                _Event.ConnectorErrorEvent(detail);
            }
        }
        if (cmd == null) {
            return;
        }

        INConnectorEvent cmdEvent = cmd.getCommandParameter().getCommandDetail().Event;

        if (_Event != null) {
            _Event.ConnectorErrorEvent(cmd, bIsStopCommand);
        }

        if (cmdEvent != null) {
            cmdEvent.ConnectorErrorEvent(cmd, bIsStopCommand);
        }
    }

    /**
     * 触发命令完成事件
     *
     * @param cmd
     */
    protected void RaiseCommandCompleteEventEvent(INCommand cmd) {

        if (_Event != null) {
            _Event.CommandCompleteEvent(cmd, null);
        }

        INConnectorEvent cmdEvent = cmd.getCommandParameter().getCommandDetail().Event;

        if (cmdEvent != null) {
            cmdEvent.CommandCompleteEvent(cmd, null);
        }
    }

    /**
     * 触发通讯错误事件
     */
    protected synchronized void fireConnectError() {
        Object[] cmds = _CommandList.toArray();
        _CommandList.clear();
        _ActivityCommand = null;

        if (cmds.length == 0) {
            RaiseConnectorErrorEvent(null, false);
        } else {
            for (Object obj : cmds) {
                INCommandRuntime cmd = (INCommandRuntime) obj;
                RaiseConnectorErrorEvent((INCommand) cmd, false);
                //cmd.Release();
            }
        }

    }

    @Override
    public synchronized void StopCommand(INIdentity idt) {
        Object[] cmds = _CommandList.toArray();
        if (idt == null) {
            _CommandList.clear();
            _ActivityCommand = null;

            for (Object obj : cmds) {
                INCommandRuntime cmd = (INCommandRuntime) obj;
                RaiseConnectorErrorEvent((INCommand) cmd, true);
                //cmd.Release();
            }

        } else {
            for (Object obj : cmds) {
                INCommandRuntime cmd = (INCommandRuntime) obj;
                INIdentity cmdIdentity = cmd.GetIdentity();
                if (idt.equals(cmdIdentity)) {
                    RaiseConnectorErrorEvent((INCommand) cmd, true);
                    if (cmd == _ActivityCommand) {
                        _ActivityCommand = null;
                    }
                    //cmd.Release();
                }
            }
        }

    }

    /**
     * 获取关于本通道的详情
     *
     * @return
     */
    protected abstract ConnectorDetail GetConnectorDetail();

    /**
     * 监控响应的处理
     */
    protected synchronized void CheckWatchResponse(ByteBuf msg) {
        if (_DecompileList.size() == 0) {
            return;
        }

        for (INWatchResponse value : _DecompileList.values()) {
            value.CheckResponse(GetConnectorDetail(), _Event, msg);
        }

    }
    
    /**
     * 用来指示任务是否已经加入任务队列，正在排队等待或者正在执行中
     * @return 
     */
    @Override
    public boolean TaskIsBegin()
    {
        return _IsRuning;
    }
    
    /**
     * 当此通道已加入任务队列时，需要调用此函数来改变此通道的任务状态
     */
    @Override
    public void SetTaskIsBegin()
    {
        _IsRuning=true;
    }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Door.Access.Command;

import Door.Access.Connector.INConnectorEvent;
import Door.Access.Packet.INPacket;
import Door.Access.Packet.INPacketDecompile;
import Door.Access.Packet.INPacketModel;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * 所有命令的抽象类
 *
 * @author 赖金杰
 */
public abstract class AbstractCommand implements INCommand {

    protected INCommandParameter _Parameter;
    /**
     * 保存命令的返回值
     */
    protected INCommandResult _Result;
    /**
     * 最大进度数<br/>
     * 当有发生变化时，应调用 {@Link AbstractCommand.RaiseCommandProcessEvent()}
     */
    protected int _ProcessMax;
    /**
     * 当前进度
     */
    protected int _ProcessStep;
    /**
     * 已发送次数
     */
    protected int _SendCount;
    /**
     * 命令是否已完结
     */
    protected boolean _IsCommandOver;
    /**
     * 最近一次发送的时间
     */
    protected Calendar _SendDate;
    /**
     * 是否需要等待接收回应
     */
    protected boolean _IsWaitResponse;
    protected INPacket _Packet;
    protected INPacketDecompile _Decompile;
    protected E_CommandStatus _Status;
    /**
     * 命令重复次数
     */
    protected int _ReSendCount;
    /**
     * 标志此命令是否已超时
     */
    protected boolean _IsTimeout;
    protected boolean _IsRelease;

    public AbstractCommand() {
        _ProcessMax = 1;
        _ProcessStep = 0;
        _SendCount = 0;
        _IsCommandOver = false;
        _SendDate = null;
        _IsWaitResponse = true;
        _Status = E_CommandStatus.OnReady;
        _ReSendCount = 0;
        _IsRelease = false;
    }

    @Override
    public int getProcessMax() {
        return _ProcessMax;
    }

    @Override
    public int getProcessStep() {
        return _ProcessStep;
    }

    @Override
    public int getSendCount() {
        return _SendCount;
    }

    @Override
    public boolean getIsCommandOver() {
        return _IsCommandOver;
    }

    @Override
    public boolean getIsTimeout() {
        return _IsTimeout;
    }

    @Override
    public void Release() {
        if (_IsRelease) {
            return;
        }
        _IsRelease = true;
        Release0();

        _Parameter = null;
        _Result = null;
        _SendDate = null;

        if (_Packet != null) {
            _Packet.Release();
        }
        _Packet = null;

        if (_Decompile != null) {
            _Decompile.Release();
        }
        _Decompile = null;
    }

    /**
     * 释放各种缓存和引用
     */
    protected abstract void Release0();

    @Override
    public INCommandParameter getCommandParameter() {
        return _Parameter;
    }

    @Override
    public void setCommandParameter(INCommandParameter par) {
        _Parameter = par;
    }

    @Override
    public INIdentity GetIdentity() {
        if (_Parameter == null) {
            return null;
        }
        return _Parameter.getCommandDetail().Identity;
    }

    @Override
    public INCommandResult getCommandResult() {
        return _Result;
    }

    @Override
    public boolean getIsWaitResponse() {
        return _IsWaitResponse;
    }

    @Override
    public boolean CheckResponse(INConnectorEvent oEvent, ByteBuf bData) {
        if (_Status != E_CommandStatus.OnWaitResponse) {
            return false;
        }
        boolean decompile = false;
        synchronized (this) {
            try {
                ArrayList<INPacketModel> oRetPack = new ArrayList<INPacketModel>(10);
                
                decompile = _Decompile.Decompile(bData, oRetPack);

                if (decompile) {

                    int iLen = oRetPack.size();
                    //System.out.println(" AbstractCommand--CheckResponse -- 解析数据包数量：" + iLen);
                    for (int i = 0; i < iLen; i++) {
                        INPacketModel model = oRetPack.get(i);

                        if (_Status == E_CommandStatus.OnWaitResponse) { //防止命令结束后继续处理，导致出现错误
                            if (CommandStep(oEvent, model)) {
                                _SendDate = Calendar.getInstance();
                            }
                        }

                        model.Release();
                    }
                } else {
                    //System.out.println(" AbstractCommand--CheckResponse -- 解析数据包失败！");
                    //bData.readerIndex(0);
                    //System.out.println(ByteBufUtil.hexDump(bData));
                }
            } catch (Exception e) {
                System.out.println(" AbstractCommand--CheckResponse -- 发生错误：\n" + e.toString());
                decompile = false;
            }
        }

        return decompile;
    }

    /**
     * 进行命令的下一步操作
     *
     * @param oEvent
     * @param model
     * @return
     */
    protected abstract boolean CommandStep(INConnectorEvent oEvent, INPacketModel model);

    @Override
    public boolean CheckTimeout(INConnectorEvent oEvent) {
        if (_Status != E_CommandStatus.OnWaitResponse) {
            return false;
        }

        if (_SendDate == null) {
            return false;
        }

        int iTimeout = 1000;
        int iMaxReSendCount = 3;

        CommandDetail detail = null;
        if (_Parameter != null) {
            detail = _Parameter.getCommandDetail();
        }

        if (detail != null) {
            iTimeout = _Parameter.getCommandDetail().Timeout;
            iMaxReSendCount = detail.RestartCount;
        }

        long lSendMillis = _SendDate.getTimeInMillis();
        long lNowMillis = Calendar.getInstance().getTimeInMillis();
        long lElapse = lNowMillis - lSendMillis;
        boolean bIsTimeout = (lElapse > iTimeout);

        if (bIsTimeout) {

            if (_ReSendCount != iMaxReSendCount) {
                //System.out.println("Door.Access.Command.AbstractCommand.CheckTimeout() -- 命令等待超时，准备重发命令！");
                CommandOver_ReSend();
                _Status = E_CommandStatus.OnReady;//变更状态为准备就绪，以便重新发送
                _ReSendCount++;

                return false;
            } else {
                //System.out.println("Door.Access.Command.AbstractCommand.CheckTimeout() -- 命令已超时！");
                _IsTimeout = true;
                CommandOver();
                RaiseCommandTimeout(oEvent);
                return true;
            }
        } else {
            return false;
        }

    }

    /**
     * 命令超时，重置资源
     */
    protected void CommandOver_ReSend() {
    }

    ;

    @Override
    public INPacket GetPacket() {
        return _Packet;
    }

    /**
     * 指示当前指令的状态
     *
     * @return
     */
    @Override
    public E_CommandStatus GetStatus() {
        return _Status;
    }

    /**
     * 命令准备就绪
     */
    protected void CommandReady() {
        _Status = E_CommandStatus.OnReady;
        _ReSendCount = 0;
    }

    /**
     * 命令继续等待。
     */
    protected void CommandWaitResponse() {
        _Status = E_CommandStatus.OnWaitResponse;
        _SendDate = Calendar.getInstance();
    }

    @Override
    public void SendCommand(INConnectorEvent oEvent) {
        _Status = E_CommandStatus.OnWaitResponse;
        if (_SendCount == 0) {
            _ProcessStep = 1;
        }
        _SendCount++;
        _SendDate = Calendar.getInstance();

        RaiseCommandProcessEvent(oEvent);
    }

    /**
     * 指令结束
     */
    protected void CommandOver() {
        _Status = E_CommandStatus.OnOver;
        _ProcessStep = _ProcessMax;

        if (_Packet != null) {
            _Packet.Release();
        }
        _Packet = null;

        if (_Decompile != null) {
            _Decompile.Release();
        }
        _Decompile = null;

        _IsCommandOver = true;

    }

    /**
     * 当命令完成时，会触发此函数回调
     *
     * @param oEvent
     */
    @Override
    public void RaiseCommandCompleteEvent(INConnectorEvent oEvent) {
//        INCommand cmd,INCommandResult result
        //System.out.println("Door.Access.Command.AbstractCommand.RaiseCommandCompleteEvent() -- 发送命令完成事件");
        CommandOver();
        if (oEvent != null) {
            oEvent.CommandCompleteEvent(this, _Result);
        }
        INConnectorEvent cmdEvent = _Parameter.getCommandDetail().Event;
        if (cmdEvent != null) {
            cmdEvent.CommandCompleteEvent(this, _Result);
        }
    }

    /**
     * 命令发生进程发生变化
     *
     * @param oEvent
     */
    @Override
    public void RaiseCommandProcessEvent(INConnectorEvent oEvent) {
        //INCommand cmd
        //System.out.println("Door.Access.Command.AbstractCommand.RaiseCommandProcessEvent() -- 发送命令进度事件");
        if (oEvent != null) {
            oEvent.CommandProcessEvent(this);
        }
        INConnectorEvent cmdEvent = _Parameter.getCommandDetail().Event;
        if (cmdEvent != null) {
            cmdEvent.CommandProcessEvent(this);
        }

    }

    /**
     * 命令超时时，触发此回到函数
     *
     * @param oEvent
     */
    protected void RaiseCommandTimeout(INConnectorEvent oEvent) {
        //INCommand cmd

        if (oEvent != null) {
            oEvent.CommandTimeout(this);
        }
        INConnectorEvent cmdEvent = _Parameter.getCommandDetail().Event;
        if (cmdEvent != null) {
            cmdEvent.CommandTimeout(this);
        }
    }

    /**
     * 通讯密码错误
     *
     * @param oEvent
     */
    protected void RaisePasswordErrorEvent(INConnectorEvent oEvent) {
        //INCommand cmd
        if (oEvent != null) {
            oEvent.PasswordErrorEvent(this);
        }
        INConnectorEvent cmdEvent = _Parameter.getCommandDetail().Event;
        if (cmdEvent != null) {
            cmdEvent.PasswordErrorEvent(this);
        }
    }

    /**
     * 通讯校验和错误
     *
     * @param oEvent
     */
    protected void RaiseChecksumErrorEvent(INConnectorEvent oEvent) {
        //INCommand cmd
        if (oEvent != null) {
            oEvent.ChecksumErrorEvent(this);
        }
        INConnectorEvent cmdEvent = _Parameter.getCommandDetail().Event;
        if (cmdEvent != null) {
            cmdEvent.ChecksumErrorEvent(this);
        }
    }
}

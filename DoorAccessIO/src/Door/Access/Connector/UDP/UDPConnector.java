/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Door.Access.Connector.UDP;

import Door.Access.Command.CommandDetail;
import Door.Access.Command.INCommand;
import Door.Access.Command.INCommandParameter;
import Door.Access.Command.INWatchResponse;
import Door.Access.Connector.AbstractConnector;
import Door.Access.Connector.ConnectorDetail;
import Door.Access.Connector.E_ConnectorStatus;
import Door.Access.Connector.E_ConnectorType;
import Door.Access.Connector.NettyAllocator;
import Door.Access.Packet.INPacket;
import Door.Access.Packet.PacketDecompileAllocator;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.DatagramPacket;
import io.netty.channel.socket.nio.NioDatagramChannel;
import io.netty.handler.timeout.IdleStateEvent;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author 赖金杰
 */
public class UDPConnector extends AbstractConnector {

    private UDPDetail _RemoteDetail; //UDP本地端口和本地绑定IP
    private Channel _UDPChannel;
    /**
     * udp子通道
     */
    private ConcurrentHashMap<String, UDPClientConnector> _Clients;

    private ConcurrentHashMap<Integer, UDPClientConnector> _BroadcastClients;
    private UDPNettyHandler _Handler; //客户端操作的操作类；
    ArrayList<String> RemoveKeyList = new ArrayList<>();
    private ChannelFuture _WriteFuture;//写操作异步状态类

    public UDPConnector(UDPDetail detail) throws CloneNotSupportedException {
        _RemoteDetail = detail.clone();
        _Clients = new ConcurrentHashMap<>();
        _BroadcastClients = new ConcurrentHashMap<>();
    }

    @Override
    protected ConnectorDetail GetConnectorDetail() {
        try {
            return _RemoteDetail.clone();
        } catch (CloneNotSupportedException ex) {
            Logger.getLogger(UDPConnector.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public E_ConnectorType GetConnectorType() {
        return E_ConnectorType.OnUDP;
    }

    @Override
    protected synchronized void CheckStatus() {
        switch (_Status) {
            case OnConnectTimeout:
            case OnError:
            case OnClosed:
                if ((_Clients.size() != 0) || _IsForcibly) {
                    UpdateActivityTime();
                    UDPBind();
                } else {
                    CheckChanelIsInvalid();//检查是否已失效
                }
                break;
            case OnConnecting://正在绑定，则忽略
                break;
            case OnConnected://已连接，表示绑定成功
                CheckSubClient();
                break;
        }

    }

    /**
     * 检查通道是否已失效 1分钟无连接，无命令任务则自动失效
     */
    @Override
    protected void CheckChanelIsInvalid() {
        if (_isRelease) {
            return;
        }
        if (_isInvalid) {
            return;
        }
        if (_IsForcibly || _Clients.size() != 0) {
            _isInvalid = false;
            return;
        }
        long lConnectMillis = _ActivityDate.getTimeInMillis();
        long lNowMillis = Calendar.getInstance().getTimeInMillis();
        long lElapse = lNowMillis - lConnectMillis;//已经过事件
        long InvalidTime = 60 * 1000;//1分钟无连接，无命令任务则自动失效
        _isInvalid = (lElapse > InvalidTime);
    }

    private void CheckSubClient() {
        RemoveKeyList.clear();
        for (String key : _Clients.keySet()) {
            try {
                UDPClientConnector uclient = _Clients.get(key);
                if (uclient.GetStatus() == E_ConnectorStatus.OnClosed) {
                    uclient.SetUDPChannel(_UDPChannel);
                }

                if (!uclient.IsInvalid()) {
                    uclient.CheckStatus();
                } else {
                    RemoveKeyList.add(key);
                }
            } catch (Exception e) {
                System.out.println("Door.Access.Connector.UDP.UDPConnector.CheckStatus()--" + e.getLocalizedMessage());
            }

        }
        for (String key : RemoveKeyList) {
            UDPClientConnector uclient = _Clients.get(key);
            _Clients.remove(key);
            _Event.ClientOffline(uclient.GetConnectorDetail());
        }
        RemoveKeyList.clear();

        if (_Clients.size() == 0) {
            CheckChanelIsInvalid();
        }
    }

    private class getClientResult {

        public UDPClientConnector Client;
        public boolean IsNew;

        public getClientResult(UDPClientConnector c, boolean n) {
            Client = c;
            IsNew = n;
        }

    }

    /**
     * 搜索在此UDP连接点下创建的子链接通道
     *
     * @param clientDTL
     * @return
     */
    public UDPClientConnector SearchClient(UDPDetail clientDTL) {
        if (this._isRelease) {
            return null;
        }
        if (_Clients == null) {
            return null;
        }

        String sKey = clientDTL.getClientKey();
        if (_Clients.containsKey(sKey)) {
            return _Clients.get(sKey);
        }

        return null;
    }

    private getClientResult getClient(String sIP, int iPort) {
        StringBuilder keybuf = new StringBuilder(100);
        keybuf.append("UDPClient:");
        keybuf.append(sIP);
        keybuf.append(":");
        keybuf.append(iPort);
        String key = keybuf.toString();
        getClientResult result;
        if (!_Clients.containsKey(key)) {
            UDPDetail uDetail = new UDPDetail(sIP, iPort, _RemoteDetail.LocalIP, _RemoteDetail.LocalPort);
            UDPClientConnector uclient = new UDPClientConnector(uDetail, _UDPChannel, _Event);
            _Clients.put(key, uclient);
            result = new getClientResult(uclient, true);

        } else {
            result = new getClientResult(_Clients.get(key), false);
        }
        if (sIP.equals("255.255.255.255")) {
            _BroadcastClients.put(iPort, _Clients.get(key));
        }
        return result;
    }

    /**
     * UDP 服务绑定
     */
    public void UDPBind() {
        if (_UDPChannel != null) {
            _UDPChannel.close();
            _UDPChannel = null;
        }

        if (_ActivityCommand == null) {
            //一个新的指令

            _ActivityCommand = _CommandList.peek();
            if (_ActivityCommand != null) {
                _ActivityCommand.RaiseCommandProcessEvent(_Event);
            }
        }
        _Status = E_ConnectorStatus.OnConnecting;
        try {
            ChannelFuture future;

            Bootstrap UDPBootstrap = new Bootstrap();
            _Handler = new UDPNettyHandler(this);
            UDPBootstrap.group(NettyAllocator.GetClientEventLoopGroup())
                    .channel(NioDatagramChannel.class)
                    .option(ChannelOption.SO_BROADCAST, true)
                    .option(ChannelOption.SO_REUSEADDR, true)
                    .handler(new ChannelInitializer<NioDatagramChannel>() {
                        @Override
                        protected void initChannel(NioDatagramChannel ch) throws Exception {

                            ChannelPipeline pipeline = ch.pipeline();
                            pipeline.addLast(_Handler);
                        }
                    });
            if (_RemoteDetail.LocalIP != null && _RemoteDetail.LocalIP.isEmpty()) {
                _RemoteDetail.LocalIP = null;
            }
            if (_RemoteDetail.LocalIP == null) {
                future = UDPBootstrap.bind(_RemoteDetail.LocalPort).sync();
            } else {
                future = UDPBootstrap.bind(_RemoteDetail.LocalIP, _RemoteDetail.LocalPort).sync();
            }

            if (future.isDone()) {
                try {
                    if (future.isCancelled()) {

                        ConnectFail();
                        _Status = E_ConnectorStatus.OnClosed;
                    } else if (future.isSuccess()) {
                        _UDPChannel = future.channel();
                        ConnectSuccess();
                    } else {
                        ConnectFail();
                    }
                } catch (Exception e) {
                }

            } else {
                ConnectFail();
            }
        } catch (Exception e) {
            System.out.println("Door.Access.Connector.UDP.UDPConnector.UDPBind()" + e.getLocalizedMessage());
            ConnectFail();
        }

    }

    public void UDPUnBind() {
        try {

            _UDPChannel.close();
            _IsForcibly = false;
            //  _UDPAllocator.notify();
            ClearNodeClient();
        } catch (Exception e) {
        }
        _Status = E_ConnectorStatus.OnClosed;
    }

    /**
     * 连接失败
     */
    private void ConnectFail() {

        if (_UDPChannel != null) {
            _UDPChannel.close();
            _UDPChannel = null;
        }

        fireConnectError();
        _Status = E_ConnectorStatus.OnError;
    }

    /**
     * 连接成功
     */
    private void ConnectSuccess() {
        _Status = E_ConnectorStatus.OnConnected;
    }

    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        if (_isRelease) {
            return;
        }
        //ConnectSuccess();
    }

    /**
     * 在连接正常或不正常关闭时发生
     *
     * @param ctx
     * @throws Exception
     */
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        if (_isRelease) {
            return;
        }

        //System.out.println("Door.Access.Connector.TCPClient.UDPConnector.channelInactive() -- 连接通道被关闭");
        if (_Status != E_ConnectorStatus.OnClosing) {
            fireConnectError();
        }
        _Status = E_ConnectorStatus.OnClosed;
        _UDPChannel = null;
    }

    /**
     * 接收到数据
     *
     * @param ctx
     * @param UDPmsg
     * @throws Exception
     */
    public void channelRead0(ChannelHandlerContext ctx, DatagramPacket UDPmsg) throws Exception {
        if (_isRelease) {
            return;
        }
        synchronized (this) {
            InetSocketAddress skt = UDPmsg.sender();
            int remotePort = skt.getPort();
            String sIP = skt.getAddress().getHostAddress();
//            System.out.println(" 收到UDP包，客户端IP：" + sIP + ":" + remotePort + ",包长度：" + UDPmsg.content().readableBytes());

            getClientResult result = getClient(sIP, remotePort);
            result.Client.channelRead0(ctx, UDPmsg);
            if (result.IsNew) {
                if (_Event != null) {
                    _Event.ClientOnline(result.Client.GetConnectorDetail());
                }
            }

            if (_BroadcastClients.containsKey(remotePort)) {
                _BroadcastClients.get(remotePort).channelRead0(ctx, UDPmsg);
            }

        }
    }

    /**
     * 线路空闲超时触发
     *
     * @param ctx
     * @param evt
     * @throws Exception
     */
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent state = (IdleStateEvent) evt;
            switch (state.state()) {
                case READER_IDLE://读空闲
                    break;
                case WRITER_IDLE: //写空闲
                    break;
            }
        } else {

        }
    }

    /**
     * 发生错误时，触发此事件
     *
     * @param ctx
     * @param cause
     * @throws Exception
     */
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        //连接发生错误，可能是连接闪断，链路故障等
        //System.out.println(" exceptionCaught -- 连接发生错误：\n" + cause.toString());

        ctx.close();
        _Status = E_ConnectorStatus.OnError;
    }

    @Override
    protected void Release0() {
        try {
            if (_Handler != null) {
                _Handler.Release();
            }
            _Handler = null;

        } catch (Exception e) {
        }

        try {
            ClearNodeClient();
            if (_UDPChannel != null) {
                if (_UDPChannel.isActive()) {
                    //通道未关闭的，立刻关闭！
                    _UDPChannel.close().sync();
                }
                _UDPChannel = null;
            }
        } catch (Exception e) {
        }

        _RemoteDetail = null;
        return;
    }

    /**
     * 清空下属所有的子节点
     */
    private void ClearNodeClient() {
        RemoveKeyList.clear();
        for (String key : _Clients.keySet()) {

            UDPClientConnector uclient = _Clients.get(key);
            uclient.SetUDPChannel(null);
            uclient.StopCommand(null);
            uclient.CloseForciblyConnect();
            RemoveKeyList.add(key);
        }
        for (String key : RemoveKeyList) {
            UDPClientConnector uclient = _Clients.get(key);
            _Clients.remove(key);
            _Event.ClientOffline(uclient.GetConnectorDetail());
        }
        RemoveKeyList.clear();

        if (_Clients.size() == 0) {
            CheckChanelIsInvalid();
        }

    }

    @Override
    public synchronized void AddWatchDecompile(ConnectorDetail detail, INWatchResponse decompile) {
        if (decompile == null) {
            return;
        }
        //首先遍历检查是否已添加过此命令解析类
        UDPDetail connDetail = (UDPDetail) detail;
        UDPClientConnector clt = getClient(connDetail.IP, connDetail.Port).Client;
        clt.AddWatchDecompile(detail, decompile);
    }

    @Override
    public synchronized void AddCommand(INCommand cmd) {

        if (cmd == null) {
            return;
        }
        INCommandParameter par = cmd.getCommandParameter();
        if (par == null) {
            return;
        }
        CommandDetail detail = par.getCommandDetail();
        if (detail == null) {
            return;
        }

        UDPDetail connDetail = (UDPDetail) detail.Connector;
        if (connDetail == null) {
            return;
        }

        UDPClientConnector clt = getClient(connDetail.IP, connDetail.Port).Client;
        // _CommandList.offer(cmd);
        //clt.AddWatchDecompile(connDetail, decompile);
        clt.AddCommand(cmd);
    }
}

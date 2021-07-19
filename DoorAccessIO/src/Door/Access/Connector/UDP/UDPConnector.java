/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Door.Access.Connector.UDP;

import Door.Access.Command.CommandDetail;
import Door.Access.Command.INCommand;
import Door.Access.Command.INCommandParameter;
import Door.Access.Connector.AbstractConnector;
import Door.Access.Connector.ConnectorDetail;
import Door.Access.Connector.E_ConnectorStatus;
import Door.Access.Connector.E_ConnectorType;
import Door.Access.Packet.INPacket;
import Door.Access.Packet.PacketDecompileAllocator;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOption;
import io.netty.channel.socket.DatagramPacket;
import io.netty.handler.timeout.IdleStateEvent;
import java.net.InetSocketAddress;
import java.util.ArrayList;
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
    
    private UDPAllocator _UDPAllocator;//UDP连接通道分配器
    private UDPDetail _RemoteDetail; //UDP本地端口和本地绑定IP
    private Channel _UDPChannel;
    /**
     * udp子通道
     */
    private  ConcurrentHashMap<String, UDPClientConnector> _Clients;
    
    private ConcurrentHashMap<Integer, UDPClientConnector> _BroadcastClients;
    private ChannelFuture _ConnectFuture;//连接的异步操作类
    private UDPNettyHandler _Handler; //客户端操作的操作类；
   ArrayList<String> RemoveKeyList=new  ArrayList<>();
    private ChannelFuture _WriteFuture;//写操作异步状态类

    public UDPConnector(UDPAllocator allocator, UDPDetail detail) throws CloneNotSupportedException {
        _RemoteDetail = detail.clone();
        _UDPAllocator = allocator;
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
     
        for (String key : _Clients.keySet()) {
           if(!_Clients.get(key).IsInvalid()){
                 _Clients.get(key).CheckStatus();
            }else{
                RemoveKeyList.add(key);
            } 
        }
        for(String key :RemoveKeyList){
            _Clients.remove(key);
        }
        RemoveKeyList.clear();
    }
    
    private UDPClientConnector getClient(String sIP, int iPort) {
        StringBuilder keybuf = new StringBuilder(100);
        keybuf.append("UDPClient:");
        keybuf.append(sIP);
        keybuf.append(":");
        keybuf.append(iPort);
        String key = keybuf.toString();
        if (!_Clients.containsKey(key)) {
            _Clients.put(key, new UDPClientConnector(new UDPDetail(sIP, iPort, _RemoteDetail.LocalIP, _RemoteDetail.LocalPort), _UDPChannel, _Event));
        }
        if (sIP.equals("255.255.255.255")) {
            _BroadcastClients.put(iPort, _Clients.get(key));
        }
        return _Clients.get(key);
    }

    /**
     * UDP 服务绑定
     */
    public void UDPBind() {
        if (_UDPAllocator == null) {
            return;
        }
        
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
            if (_RemoteDetail.LocalIP == null) {
                _ConnectFuture = _UDPAllocator.Bind(_RemoteDetail.LocalPort);
            } else {
                _ConnectFuture = _UDPAllocator.Bind(_RemoteDetail.LocalIP, _RemoteDetail.LocalPort);
            }
            
            _UDPChannel = _ConnectFuture.channel();
            
            if (_ConnectFuture.isDone()) {
                try {
                    new connectCallback(this).operationComplete(_ConnectFuture);
                } catch (Exception e) {
                }
                
            } else {
                _ConnectFuture.addListener(new connectCallback(this));
            }
        } catch (Exception e) {
            System.out.println("Door.Access.Connector.UDP.UDPConnector.UDPBind()" + e.getLocalizedMessage());
            _Status = E_ConnectorStatus.OnError;
        }
        
    }

    /**
     * 处理通讯连接回调；
     */
    private class connectCallback implements ChannelFutureListener {
        
        private UDPConnector _UDP;
        
        public connectCallback(UDPConnector udp) {
            _UDP = udp;
        }
        
        @Override
        public void operationComplete(ChannelFuture future) throws Exception {
            if (_UDP == null) {
                return;
            }
            if (_isRelease) {
                return;
            }
            if (future.isDone()) {
                if (future.isCancelled()) {
                    
                    _UDP.ConnectFail();
                    _UDP._Status = E_ConnectorStatus.OnClosed;
                } else if (future.isSuccess()) {
                    _UDP._Handler = new UDPNettyHandler(_UDP);
                    future.channel().pipeline().addLast(_UDP._Handler);//命令处理
                    ConnectSuccess();
                } else {
                    
                    _UDP.ConnectFail();
                }
                _UDP._ConnectFuture = null;
                _UDP = null;
            }
        }
    }

    /**
     * 连接失败
     */
    private void ConnectFail() {
        if (_UDPChannel == null) {
            return;
        }
        _UDPChannel.close();
        _UDPChannel = null;
        
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
            if (_BroadcastClients.containsKey(remotePort)) {
                _BroadcastClients.get(remotePort).channelRead0(ctx, UDPmsg);
            }
            
            getClient(sIP, remotePort).channelRead0(ctx, UDPmsg);
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
            if (_ConnectFuture != null) {
                _ConnectFuture.cancel(true);
                _ConnectFuture.sync();
            }
            _ConnectFuture = null;
            
        } catch (Exception e) {
        }
        
        try {
            if (_UDPChannel != null) {
                if (_UDPChannel.isActive()) {
                    //通道未关闭的，立刻关闭！
                    _UDPChannel.close().sync();
                }
                _UDPChannel = null;
            }
        } catch (Exception e) {
        }
        
        _UDPAllocator = null;
        _RemoteDetail = null;
        return;
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
        
        UDPClientConnector clt = getClient(connDetail.IP, connDetail.Port);
        clt.AddCommand(cmd);
    }
}

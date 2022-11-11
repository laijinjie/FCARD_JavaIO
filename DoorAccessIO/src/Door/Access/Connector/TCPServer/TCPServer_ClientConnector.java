/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Door.Access.Connector.TCPServer;

import Door.Access.Connector.AbstractConnector;
import Door.Access.Connector.ConnectorDetail;
import Door.Access.Connector.E_ConnectorStatus;
import Door.Access.Connector.E_ConnectorType;
import Door.Access.Connector.INConnectorEvent;
import Door.Access.Connector.TCPClient.TCPClientConnector;
import Door.Access.Data.BytesData;
import Door.Access.Data.INData;
import Door.Access.Packet.INPacket;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.timeout.IdleStateEvent;
import java.net.InetSocketAddress;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * TCP服务器中的客户端处理类
 *
 * @author 赖金杰
 */
public class TCPServer_ClientConnector extends AbstractConnector {

    /**
     * 本通道的身份标识
     */
    private final int _ClientID;
    private TCPServerClientDetail _RemoteDetail;//包含有本地身份信息
    private SocketChannel _Client;
    protected TCPServerAllocator _ServerAllocator;//连接分配器，用于断线时的通知

    private TCPServer_ClientNettyHandler _Handler; //客户端操作的操作类；
    private ChannelFuture _WriteFuture;//写操作异步状态类

    public TCPServer_ClientConnector(TCPServerAllocator allocator, INConnectorEvent event, SocketChannel ch, TCPServer_ClientNettyHandler handler) throws CloneNotSupportedException {
        //创建客户端通道身份标志

        _ClientID = TCPServerAllocator.GetNewClientID();
        InetSocketAddress localAddress, remoteAddress;
        localAddress = ch.localAddress();
        remoteAddress = ch.remoteAddress();
        String loacalip = localAddress.getHostString();
        if (loacalip.equals("0:0:0:0:0:0:0:0")) {
            loacalip = "0.0.0.0";
        }

        _RemoteDetail = new TCPServerClientDetail(_ClientID);
        _RemoteDetail.Local = new IPEndPoint(loacalip, localAddress.getPort());
        _RemoteDetail.Remote = new IPEndPoint(remoteAddress.getHostString(), remoteAddress.getPort());

        SetEventHandle(event);
        _ServerAllocator = allocator;
        _Client = ch;
        _Handler = handler;

        _Status = E_ConnectorStatus.OnConnected;
    }

    /**
     * 代表了本通道的身份
     *
     * @return
     */
    public int ClientID() {
        return _ClientID;
    }

    @Override
    protected ConnectorDetail GetConnectorDetail() {
        try {
            return _RemoteDetail.clone();
        } catch (CloneNotSupportedException ex) {
            Logger.getLogger(TCPServer_ClientConnector.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public E_ConnectorType GetConnectorType() {
        return E_ConnectorType.OnTCPServer_Client;
    }

    @Override
    protected synchronized void CheckStatus() {
        switch (_Status) {
            case OnConnectTimeout:
            case OnClosed:
                //发现链接已关闭，则发出链接错误消息
                fireConnectError();
                break;
            case OnConnected:
                //已连接则检查命令是否已准备好
                _ActivityCommand = _CommandList.peek();
                if (_ActivityCommand != null) {
                    switch (_ActivityCommand.GetStatus()) {
                        case OnReady:
                            //System.out.println("Door.Access.Connector.TCPClient.TCPClientConnector.CheckStatus() -- 准备发送指令");
                            //发送数据
                            INPacket send = _ActivityCommand.GetPacket();
                            ByteBuf packetBuf = send.GetPacketData();
                            packetBuf.markReaderIndex();
                            ByteBuf sendBuf = _Client.alloc().buffer(packetBuf.readableBytes());
                            sendBuf.writeBytes(packetBuf);
                            packetBuf.resetReaderIndex();
                            //System.out.println("发送指令：" + ByteBufUtil.hexDump(sendBuf));
                            _WriteFuture = _Client.writeAndFlush(sendBuf);
                            _ActivityCommand.SendCommand(_Event);
                            _WriteFuture.addListener(new WriteCallback(this));

                            break;
                        case OnWaitResponse:
                            //检查超时
                            if (_ActivityCommand.CheckTimeout(_Event)) {
                                //已超时，发送超时消息
                                _CommandList.poll();
                                //_ActivityCommand.Release();
                                _ActivityCommand = null;
                            }
                    }
                }
                break;
        }
    }

    private class WriteCallback implements ChannelFutureListener {

        TCPServer_ClientConnector _Client;

        public WriteCallback(TCPServer_ClientConnector client) {
            _Client = client;
        }

        @Override
        public void operationComplete(ChannelFuture f) throws Exception {
            if (_Client._isRelease) {
                return;
            }
            if (f.isDone()) {
                if (_Client._ActivityCommand == null) {
                    return;
                }

                if (f.isSuccess()) {//写成功
                    synchronized (_Client) {
                        try {
                            if (!_Client._ActivityCommand.getIsWaitResponse()) {
                                _Client._CommandList.poll();
                                _Client._ActivityCommand.RaiseCommandCompleteEvent(_Event);
                                _Client._ActivityCommand = null;
                            }

                        } catch (Exception e) {
                        }

                    }
                } else if (f.isCancellable()) { //取消写
                    System.out.println("WriteCallback -- 取消写！");
                } else {//写失败,表示连接已失效
                    System.out.println("WriteCallback -- 写失败,表示连接已失效！");
                }
                _Client = null;
                return;

            }
        }
    }

    /**
     * 强制关闭通道
     */
    public void Close() {
        if (_Client != null) {
            if (_Client.isActive()) {
                _Client.close();
            }
        }

    }

    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        if (_isRelease) {
            return;
        }

        ConnectSuccess();
    }

    public void ConnectSuccess() {
        _Status = E_ConnectorStatus.OnConnected;
        if (_Event != null) {
            _Event.ClientOnline(_RemoteDetail);
        }
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

        Offline();
        _Client = null;
    }

    /**
     * 离线操作
     */
    public void Offline() {
        if (_Status != E_ConnectorStatus.OnClosed) {
            //有未发送的指令，则立刻返回失败消息
            fireConnectError();
        } else {
            return;
        }

        _Status = E_ConnectorStatus.OnClosed;
        if (_Event != null) {
            _Event.ClientOffline(_RemoteDetail);
        }
        /**
         * 从本地列表中删除此客户端
         */
        if (_ServerAllocator != null) {
            _ServerAllocator.DeleteClient(_ClientID);
        }
        //释放资源
        Release();

    }

    /**
     * 接收到数据
     *
     * @param ctx
     * @param msg
     * @throws Exception
     */
    public void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
        if (_isRelease) {
            return;
        }
        synchronized (this) {
            try {
                if (_ActivityCommand != null) {
                    //System.out.println(" 已收到数据：\n" + ByteBufUtil.hexDump(msg));
                    boolean ret = _ActivityCommand.CheckResponse(_Event, msg);
                    if (ret) {
                        if (_ActivityCommand.getIsCommandOver()) {
                            synchronized (this) {//命令已完结，删除
                                _CommandList.poll();
                                //_ActivityCommand.Release();
                                _ActivityCommand = null;
                            }
                        }
                    }
                }
            } catch (Exception e) {
                //System.out.println(" channelRead0 -- 1 发生错误：\n" + e.toString());
            }

            try {
                if (_DecompileList.size() > 0) {
                    CheckWatchResponse(msg);
                } else {
                    if (_Event != null) {
                        INData bd = new BytesData();
                        bd.SetBytes(msg);
                        _Event.WatchEvent(_RemoteDetail, bd);
                    }
                }

            } catch (Exception e) {
                //System.out.println(" channelRead0 --2 发生错误：\n" + e.toString());
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
                    ByteBuf sendBuf=_Client.alloc().buffer(TCPServerAllocator.KeepAliveMsg.length);
                    sendBuf.writeBytes(TCPServerAllocator.KeepAliveMsg);
                    _WriteFuture = _Client.writeAndFlush(sendBuf);
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
        Offline();
    }

    @Override
    protected void Release0() {
        Offline();

        try {
            if (_Handler != null) {
                _Handler.Release();
            }
            _Handler = null;

        } catch (Exception e) {
        }

        try {
            if (_Client != null) {
                if (_Client.isActive()) {
                    //通道未关闭的，立刻关闭！
                    _Client.close();
                }
                _Client = null;
            }
        } catch (Exception e) {
        }
        return;
    }

}

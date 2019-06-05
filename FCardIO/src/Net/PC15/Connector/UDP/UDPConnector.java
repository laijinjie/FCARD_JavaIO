/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Net.PC15.Connector.UDP;

import Net.PC15.Connector.AbstractConnector;
import Net.PC15.Connector.ConnectorDetail;
import Net.PC15.Connector.E_ConnectorStatus;
import Net.PC15.Connector.E_ConnectorType;
import Net.PC15.Packet.INPacket;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOption;
import io.netty.channel.socket.DatagramPacket;
import io.netty.handler.timeout.IdleStateEvent;
import java.net.InetSocketAddress;
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

    private ChannelFuture _ConnectFuture;//连接的异步操作类
    private UDPNettyHandler _Handler; //客户端操作的操作类；

    private ChannelFuture _WriteFuture;//写操作异步状态类

    public UDPConnector(UDPAllocator allocator, UDPDetail detail) throws CloneNotSupportedException {
        _RemoteDetail = detail.clone();
        _UDPAllocator = allocator;
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
        return E_ConnectorType.OnTCPClient;
    }

    @Override
    protected synchronized void CheckStatus() {
        switch (_Status) {
            case OnConnectTimeout:
            case OnClosed:
                if ((_CommandList.peek() != null) || _IsForcibly) {
                    UpdateActivityTime();
                    UDPBind();
                }
                else
                {
                    CheckChanelIsInvalid();
                }
                break;
            case OnConnected:
                //已连接则检查命令是否已准备好
                _ActivityCommand = _CommandList.peek();
                if (_ActivityCommand != null) {
                    UpdateActivityTime();
                    switch (_ActivityCommand.GetStatus()) {
                        case OnReady:
                            //System.out.println("Net.PC15.Connector.TCPClient.UDPConnector.CheckStatus() -- 准备发送指令");
                            UDPDetail uDetail = (UDPDetail) _ActivityCommand.getCommandParameter().getCommandDetail().Connector;
                            //发送数据
                            INPacket send = _ActivityCommand.GetPacket();
                            ByteBuf packetBuf = send.GetPacketData();
                            packetBuf.markReaderIndex();
                            ByteBuf sendBuf = _UDPChannel.alloc().buffer(packetBuf.readableBytes());
                            sendBuf.writeBytes(packetBuf);
                            packetBuf.resetReaderIndex();
                            //System.out.println("发送指令：" + ByteBufUtil.hexDump(sendBuf));
                            try {
                                InetSocketAddress remote = new InetSocketAddress(uDetail.IP, uDetail.Port);
                                _UDPChannel.config().setOption(ChannelOption.SO_BROADCAST, uDetail.Broadcast);

                                DatagramPacket dp = new DatagramPacket(sendBuf, remote);
                                _WriteFuture = _UDPChannel.writeAndFlush(dp);
                                _ActivityCommand.SendCommand(_Event);
                                _WriteFuture.addListener(new WriteCallback(this));
                            } catch (Exception e) {
                            }

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
                } else {
                    //检查是否需要关闭通道
                    if (_IsForcibly) {
                        UpdateActivityTime();
                        return;
                    }
                    _Status = E_ConnectorStatus.OnClosing;
                    _UDPChannel.close();//关闭连接
                }
                break;
        }
    }

    private class WriteCallback implements ChannelFutureListener {

        UDPConnector _Client;

        public WriteCallback(UDPConnector client) {
            _Client = client;
        }

        @Override
        public void operationComplete(ChannelFuture f) throws Exception {
            if (_isRelease) {
                return;
            }
            if (f.isDone()) {
                if (_ActivityCommand == null) {
                    return;
                }

                if (f.isSuccess()) {//写成功
                    synchronized (_Client) {
                        try {
                            if (!_ActivityCommand.getIsWaitResponse()) {
                                _CommandList.poll();
                                _ActivityCommand.RaiseCommandCompleteEvent(_Event);
                                //_ActivityCommand.Release();
                                _ActivityCommand = null;
                            }

                        } catch (Exception e) {
                        }

                    }
                } else if (f.isCancellable()) { //取消写
                    System.out.println("WriteCallback -- 取消写！");
                } else {//写失败,表示连接已失效
                    System.out.println("UDP发送数据报失败，可能ip地址格式错误或参数不正确");
                }
                _Client = null;
                return;

            }
        }
    }

    /**
     * UDP 服务绑定
     */
    private void UDPBind() {
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
            System.out.println("Net.PC15.Connector.UDP.UDPConnector.UDPBind()" + e.getLocalizedMessage());
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

        //System.out.println("Net.PC15.Connector.TCPClient.UDPConnector.channelInactive() -- 连接通道被关闭");
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
     * @param msg
     * @throws Exception
     */
    public void channelRead0(ChannelHandlerContext ctx, DatagramPacket UDPmsg) throws Exception {
        if (_isRelease) {
            return;
        }
        synchronized (this) {
            ByteBuf msg = UDPmsg.content();
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
                CheckWatchResponse(msg);
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

}

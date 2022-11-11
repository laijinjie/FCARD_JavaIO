package Door.Access.Connector.TCPClient;

import Door.Access.Command.E_CommandStatus;
import Door.Access.Connector.AbstractConnector;
import Door.Access.Connector.ConnectorDetail;
import Door.Access.Connector.E_ConnectorStatus;
import Door.Access.Connector.E_ConnectorType;
import Door.Access.Connector.TCPServer.TCPServerAllocator;
import Door.Access.Packet.INPacket;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleStateEvent;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * TCP客户端连接器
 *
 * @author 赖金杰
 */
public class TCPClientConnector extends AbstractConnector {

    private TCPClientAllocator _ClientAllocator;//tcp连接通道分配器
    private TCPClientDetail _RemoteDetail; //远程IP信息和端口信息
    private Channel _ClientChannel;

    private Calendar _ConnectDate;//连接开始时间
    private boolean _IsCancelConnect;//连接是否已取消？
    private ChannelFuture _ConnectFuture;//连接的异步操作类
    private int _ConnectTimeoutMSEL, //最大连接等待时间
            _ReconnectMax,//最大重新连接次数
            _ConnectFailCount;//连接超时时间和连接失败次数

    private TCPClientNettyHandler _Handler; //客户端操作的操作类；

    private ChannelFuture _WriteFuture;//写操作异步状态类

    public TCPClientConnector(TCPClientAllocator allocator, TCPClientDetail detail) throws CloneNotSupportedException {
        _RemoteDetail = detail.clone();
        _ConnectTimeoutMSEL = detail.Timeout;
        SetConnectOption(detail);
        _ConnectFailCount = 0;
        _ClientAllocator = allocator;
    }

    /**
     * 设置连接参数，超时上限和重连上限
     *
     * @param detail
     */
    private void SetConnectOption(TCPClientDetail detail) {
        _ConnectTimeoutMSEL = detail.Timeout;
        _ReconnectMax = detail.RestartCount;

        if (_ConnectTimeoutMSEL > TCPClientAllocator.CONNECT_TIMEOUT_MILLIS_MAX) {
            _ConnectTimeoutMSEL = TCPClientAllocator.CONNECT_TIMEOUT_MILLIS_MAX;
        } else if (_ConnectTimeoutMSEL < TCPClientAllocator.CONNECT_TIMEOUT_MILLIS_MIN) {
            _ConnectTimeoutMSEL = TCPClientAllocator.CONNECT_TIMEOUT_MILLIS_MIN;
        }

        if (_ReconnectMax > TCPClientAllocator.CONNECT_RECONNECT_MAX) {
            _ReconnectMax = TCPClientAllocator.CONNECT_RECONNECT_MAX;
        } else if (_ReconnectMax < 0) {
            _ReconnectMax = 0;
        }
    }

    @Override
    protected ConnectorDetail GetConnectorDetail() {
        try {
            return _RemoteDetail.clone();
        } catch (CloneNotSupportedException ex) {
            Logger.getLogger(TCPClientConnector.class.getName()).log(Level.SEVERE, null, ex);
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
                    ConnectServer();
                } else {
                    CheckChanelIsInvalid();//检查是否已失效
                }
                break;
            case OnConnecting:
                UpdateActivityTime();
                //检查连接是否超时
                if (!_IsCancelConnect) {//检查连接是否已取消
                    if (IsConnectTimeout()) {//连接未取消，则检查连接是否超时
                        if (_ConnectFuture != null) {
                            _ConnectFuture.cancel(true);
                            _IsCancelConnect = true;
                        } else {
                            if (_ClientChannel != null) {
                                try {
                                    _Status = E_ConnectorStatus.OnConnectTimeout;
                                    _ClientChannel.close().sync();
                                } catch (Exception e) {
                                }

                            }
                        }

                    }
                }
                break;
            case OnConnected:

                //已连接则检查命令是否已准备好
                _ActivityCommand = _CommandList.peek();
                if (_ActivityCommand != null) {
                    UpdateActivityTime();
                    switch (_ActivityCommand.GetStatus()) {
                        case OnReady:
                            //System.out.println("Door.Access.Connector.TCPClient.TCPClientConnector.CheckStatus() -- 准备发送指令");
                            //发送数据
                            INPacket send = _ActivityCommand.GetPacket();
                            ByteBuf packetBuf = send.GetPacketData();
                         //   System.out.println(packetBuf);
                         //   System.out.println(ByteBufUtil.hexDump(packetBuf));  
                            packetBuf.markReaderIndex();
                            ByteBuf sendBuf = _ClientChannel.alloc().buffer(packetBuf.readableBytes());
                            sendBuf.writeBytes(packetBuf);
                            packetBuf.resetReaderIndex();
                           //  System.out.println("发送指令：" + ByteBufUtil.hexDump(sendBuf));
                            _WriteFuture = _ClientChannel.writeAndFlush(sendBuf);
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
                              //  System.out.println("已超时，发送超时消息");
                            }
                    }
                } else {
                    //检查是否需要关闭通道
                    if (_IsForcibly) {
                        UpdateActivityTime();
                        return;
                    }
                    _Status = E_ConnectorStatus.OnClosing;
                    _ClientChannel.close();//关闭连接
                }
                break;
        }
    }

    private class WriteCallback implements ChannelFutureListener {

        TCPClientConnector _Client;

        public WriteCallback(TCPClientConnector client) {
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
                    System.out.println("WriteCallback -- 写失败,表示连接已失效！");
                }
                _Client = null;
                return;

            }
        }
    }

    /**
     * 检查连接是否超时
     *
     * @return
     */
    private boolean IsConnectTimeout() {
        long lConnectMillis = _ConnectDate.getTimeInMillis();
        long lNowMillis = Calendar.getInstance().getTimeInMillis();
        long lElapse = lNowMillis - lConnectMillis;//已经过事件
        return (lElapse > _ConnectTimeoutMSEL);
    }

    /**
     * 连接到服务器
     */
    private void ConnectServer() {
        if (_ClientAllocator == null) {
            return;
        }

        if (_ClientChannel != null) {
            _ClientChannel.close();
            _ClientChannel = null;
        }

        if (_ActivityCommand == null) {
            //一个新的指令

            _ActivityCommand = _CommandList.peek();
            if (_ActivityCommand != null) {
                _ConnectFailCount = 0;
                _ActivityCommand.RaiseCommandProcessEvent(_Event);
            }
        }
        //System.out.println("Door.Access.Connector.TCPClient.TCPClientConnector.ConnectServer() -- 准备连接服务器：" + _RemoteDetail.IP + ":" + _RemoteDetail.Port);
        _Status = E_ConnectorStatus.OnConnecting;
        int time = 3000;
        if (_ActivityCommand != null) {
            time = _ActivityCommand.getCommandParameter().getCommandDetail().Connector.Timeout;
        }
        _ConnectFuture = _ClientAllocator.connect(_RemoteDetail.IP, _RemoteDetail.Port, time);
        _ClientChannel = _ConnectFuture.channel();
        _ConnectDate = Calendar.getInstance();//设置连接时间
        _IsCancelConnect = false;

        if (_ConnectFuture.isDone()) {
            try {
                new connectCallback(this).operationComplete(_ConnectFuture);
            } catch (Exception e) {
            }

        } else {

            _ConnectFuture.addListener(new connectCallback(this));

        }

    }

    /**
     * 处理通讯连接回调；
     */
    private class connectCallback implements ChannelFutureListener {

        private TCPClientConnector _tcpClient;

        public connectCallback(TCPClientConnector tcp) {
            _tcpClient = tcp;
        }

        @Override
        public void operationComplete(ChannelFuture future) throws Exception {
            if (_tcpClient == null) {
                return;
            }
            if (_isRelease) {
                return;
            }
            if (future.isDone()) {
                if (future.isCancelled()) {
                    //System.out.println("Door.Access.Connector.TCPClient.TCPClientConnector.connectCallback.operationComplete() -- 服务器连接已取消");
                    _tcpClient.ConnectFail();
                    _tcpClient._Status = E_ConnectorStatus.OnClosed;
                } else if (future.isSuccess()) {
                    //System.out.println("Door.Access.Connector.TCPClient.TCPClientConnector.connectCallback.operationComplete() -- 服务器连接完毕");
                    _tcpClient._Handler = new TCPClientNettyHandler(_tcpClient);
                    future.channel().pipeline().addLast(_tcpClient._Handler);//命令处理
                    ConnectSuccess();
                } else {
                    //System.out.println("Door.Access.Connector.TCPClient.TCPClientConnector.connectCallback.operationComplete() -- 服务器连接失败");
                    _tcpClient.ConnectFail();
                }
                _tcpClient._ConnectFuture = null;
                _tcpClient = null;
            }
        }
    }

    /**
     * 连接失败
     */
    private void ConnectFail() {
        if (_ClientChannel == null) {
            return;
        }
        _Status = E_ConnectorStatus.OnConnectTimeout;
        _ClientChannel.close();
        _ClientChannel = null;
        _ConnectFailCount++;
        if (_ConnectFailCount >= _ReconnectMax) {
            try {
                fireConnectError();

            } catch (Exception e) {
                //System.out.println("Door.Access.Connector.TCPClient.TCPClientConnector.ConnectFail()" + e.toString());
            }
            _Status = E_ConnectorStatus.OnError;
        }
    }

    /**
     * 连接成功
     */
    private void ConnectSuccess() {
        //System.out.println("Door.Access.Connector.TCPClient.TCPClientConnector.ConnectSuccess() -- 连接成功");
        _Status = E_ConnectorStatus.OnConnected;
        _ConnectFailCount = 0;
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

        //System.out.println("Door.Access.Connector.TCPClient.TCPClientConnector.channelInactive() -- 连接通道被关闭");
        if (_Status != E_ConnectorStatus.OnClosing) {
            fireConnectError();
        }
        _Status = E_ConnectorStatus.OnClosed;
        _ClientChannel = null;
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
                    // System.out.println(" 已收到数据：\n" + ByteBufUtil.hexDump(msg));
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

                    ByteBuf sendBuf=_ClientChannel.alloc().buffer(TCPServerAllocator.KeepAliveMsg.length);
                    sendBuf.writeBytes(TCPServerAllocator.KeepAliveMsg);
                     _WriteFuture = _ClientChannel.writeAndFlush(sendBuf);
                   //  _WriteFuture.addListener(new WriteKeepAliveCallback(this,ctx));
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
            if (_ClientChannel != null) {
                if (_ClientChannel.isActive()) {
                    //通道未关闭的，立刻关闭！
                    _ClientChannel.close().sync();
                }
                _ClientChannel = null;
            }
        } catch (Exception e) {
        }

        _ClientAllocator = null;
        _RemoteDetail = null;
        return;
    }

}

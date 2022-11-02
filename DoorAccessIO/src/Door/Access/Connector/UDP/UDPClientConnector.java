/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Door.Access.Connector.UDP;

import Door.Access.Command.INCommand;
import Door.Access.Command.INWatchResponse;
import Door.Access.Connector.AbstractConnector;
import Door.Access.Connector.ConnectorDetail;
import Door.Access.Connector.E_ConnectorStatus;
import Door.Access.Connector.E_ConnectorType;
import Door.Access.Connector.INConnectorEvent;
import Door.Access.Data.BytesData;
import Door.Access.Data.INData;
import Door.Access.Packet.INPacket;
import Door.Access.Packet.PacketDecompileAllocator;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.DatagramPacket;
import java.net.InetSocketAddress;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 *
 * @author F
 */
public class UDPClientConnector extends AbstractConnector {
    private UDPDetail _RemoteDetail; //UDP本地端口和本地绑定IP
    private Channel _UDPChannel;//通道
    private ChannelFuture _WriteFuture;//写操作异步状态类
    /**
     * 本通道的身份标识
     */

    public UDPClientConnector(UDPDetail detail, Channel _Channel, INConnectorEvent ev) {
      
        try {           
            _RemoteDetail = detail.clone();
        } catch (Exception e) {
        }
        _UDPChannel = _Channel;
        _Status = E_ConnectorStatus.OnConnected;
        _Event = ev;
      //  _IsForcibly=true;
    }

    @Override
    protected void Release0() {
       // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected void CheckStatus() {
        switch (_Status) {
            case OnConnectTimeout:
            case OnClosed:

                break;
            case OnConnected:
                //已连接则检查命令是否已准备好
                _ActivityCommand = _CommandList.peek();
                if (_ActivityCommand != null) {
                    UpdateActivityTime();
                    switch (_ActivityCommand.GetStatus()) {
                        case OnReady:
                            //System.out.println("Door.Access.Connector.TCPClient.UDPConnector.CheckStatus() -- 准备发送指令");
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

                                DatagramPacket dp = new DatagramPacket(sendBuf, remote);
                                _WriteFuture = _UDPChannel.writeAndFlush(dp);
                                _ActivityCommand.SendCommand(_Event);
                                _WriteFuture.addListener(new UDPClientConnector.WriteCallback(this));
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
                    if ((_CommandList.peek() != null) || _IsForcibly) {
                        UpdateActivityTime();

                    } else {
                        CheckChanelIsInvalid();
                    }
                }
                break;
        }
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

    @Override
    protected ConnectorDetail GetConnectorDetail() {
        return _RemoteDetail;
    }
    
    public String GetKey(){
        return _RemoteDetail.getClientKey();
    }
  /**
     * 强制关闭通道
     */
    public void Close() {
        if (_UDPChannel != null) {
            if (_UDPChannel.isActive()) {
                _UDPChannel.close();
            }
        }

    }
    @Override
    public E_ConnectorType GetConnectorType() {
        return E_ConnectorType.OnUDP;
    }

    private class WriteCallback implements ChannelFutureListener {

        UDPClientConnector _Client;

        public WriteCallback(UDPClientConnector client) {
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

            }
        }
    }
    
}

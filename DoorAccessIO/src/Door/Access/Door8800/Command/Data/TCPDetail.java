/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Door.Access.Door8800.Command.Data;

import Door.Access.Data.INData;
import Door.Access.Util.ByteUtil;
import Door.Access.Util.StringUtil;
import Door.Access.Util.UInt32Util;
import io.netty.buffer.ByteBuf;

/**
 * TCP网络参数详情
 *
 * @author 赖金杰
 */
public class TCPDetail implements INData {

    @Override
    public int GetDataLen() {
        return 0x89;
    }

    @Override
    public void SetBytes(ByteBuf data) {
        StringBuilder strbuilder = new StringBuilder(30);
        strbuilder.reverse();
        //data.markReaderIndex();
        //MAC地址
        for (int i = 0; i < 6; i++) {
            short iMacValue = data.readUnsignedByte();
            String hex = Integer.toString( iMacValue,16);
            strbuilder.append(hex);
            if (i < 5) {
                strbuilder.append("-");
            }
        }

        mMAC = strbuilder.toString();

        //IP地址
        strbuilder.delete(0, strbuilder.length());
        ReadIPByByteBuf(data, strbuilder);
        mIP = strbuilder.toString();

        //IP掩码
        strbuilder.delete(0, strbuilder.length());
        ReadIPByByteBuf(data, strbuilder);
        mIPMask = strbuilder.toString();

        //网关
        strbuilder.delete(0, strbuilder.length());
        ReadIPByByteBuf(data, strbuilder);
        mIPGateway = strbuilder.toString();

        //DNS
        strbuilder.delete(0, strbuilder.length());
        ReadIPByByteBuf(data, strbuilder);
        mDNS = strbuilder.toString();

        //DNS
        strbuilder.delete(0, strbuilder.length());
        ReadIPByByteBuf(data, strbuilder);
        mDNSBackup = strbuilder.toString();

        //控制器网络协议类型
        mProtocolType = data.readUnsignedByte();

        //控制器使用的TCP端口
        mTCPPort = data.readUnsignedShort();
        //控制器使用的UDP端口
        mUDPPort = data.readUnsignedShort();
        //控制器作为客户端时，目标服务器的端口
        mServerPort = data.readUnsignedShort();
        //控制器作为客户端时，目标服务器的IP 
        strbuilder.delete(0, strbuilder.length());
        ReadIPByByteBuf(data, strbuilder);
        mServerIP = strbuilder.toString();
        //自动获得IP
        mAutoIP = (data.readByte() == 1);
        //控制器作为客户端时，目标服务器的域名
        int iLen = data.readableBytes();
        int iReadIndex = data.readerIndex();
        int iCharCount = 0;
        byte bValue = 0;
        for (int i = 0; i < iLen; i++) {
            bValue = data.readByte();
            if (bValue == 0) {
                break;
            } else {
                iCharCount++;
            }
        }
        data.readerIndex(iReadIndex);
        if (iCharCount == 0) {
            mServerAddr = null;
        } else {
            byte tmp[] = new byte[iCharCount];
            data.readBytes(tmp, 0, iCharCount);
            mServerAddr = new String(tmp);
        }

        //data.resetReaderIndex();
        strbuilder = null;
        return;
    }

    /**
     * 从 ByteBuf 中读取一个IP地址
     *
     * @param data
     * @param strbuilder
     */
    private void ReadIPByByteBuf(ByteBuf data, StringBuilder strbuilder) {
        for (int i = 0; i < 4; i++) {
            strbuilder.append(data.readUnsignedByte());
            if (i < 3) {
                strbuilder.append(".");
            }
        }

    }

    @Override
    public ByteBuf GetBytes() {
        ByteBuf buf = ByteUtil.ALLOCATOR.buffer(0x89);
        SaveMACToByteBuf(mMAC, buf);
        SaveIPToByteBuf(mIP, buf);
        SaveIPToByteBuf(mIPMask, buf);
        SaveIPToByteBuf(mIPGateway, buf);
        SaveIPToByteBuf(mDNS, buf);
        SaveIPToByteBuf(mDNSBackup, buf);
        buf.writeByte(mProtocolType);
        buf.writeShort(mTCPPort);
        buf.writeShort(mUDPPort);
        buf.writeShort(mServerPort);
        SaveIPToByteBuf(mServerIP, buf);
        buf.writeByte(ByteUtil.BoolToByte(mAutoIP));

        if (StringUtil.IsNullOrEmpty(mServerAddr)) {
            Save0toByteBuf(buf, 99);
        } else {
            byte[] tmp = mServerAddr.getBytes();
            buf.writeBytes(tmp);
            int iCount = 99 - tmp.length;
            if (iCount > 0) {
                Save0toByteBuf(buf, iCount);
            }
        }
        return buf;
    }

    private void Save0toByteBuf(ByteBuf buf, int iCount) {
        for (int i = 0; i < iCount; i++) {
            buf.writeByte(0);
        }
    }

    /**
     * 将MAC地址保存到ByteBuf中
     *
     * @param mac
     * @param buf
     */
    private void SaveMACToByteBuf(String mac, ByteBuf buf) {

        if (!CheckMAC(mac)) {
            for (int i = 0; i < 6; i++) {
                buf.writeByte(0);
            }
            return;
        }
        String[] macList = mac.split("-");
        int iLen = macList.length;
        for (int i = 0; i < iLen; i++) {
            buf.writeByte(Integer.parseInt(macList[i],16));
        }

    }

    /**
     * 将IP地址保存到ByteBuf中
     *
     * @param IP
     * @param buf
     */
    private void SaveIPToByteBuf(String IP, ByteBuf buf) {

        if (!CheckIP(IP)) {
            for (int i = 0; i < 4; i++) {
                buf.writeByte(0);
            }
            return;
        }
        String[] ipList = IP.split("\\.");
        int iLen = ipList.length;
        for (int i = 0; i < iLen; i++) {
            buf.writeByte(Integer.parseInt(ipList[i]));
        }

    }

    //MAC地址
    private String mMAC;
    //IP地址
    private String mIP;
    //IP掩码
    private String mIPMask;
    //网关
    private String mIPGateway;
    //DNS
    private String mDNS;
    //备用DNS
    private String mDNSBackup;
    //控制器网络协议类型：1--TCP  client （控制器就是 Client）;2--TCP Server（控制器就是 Server）；
    private short mProtocolType;
    //控制器使用的TCP端口
    private int mTCPPort;
    //控制器使用的UDP端口
    private int mUDPPort;
    //控制器作为客户端时，目标服务器的端口
    private int mServerPort;
    //控制器作为客户端时，目标服务器的IP    
    private String mServerIP;
    //自动获得IP
    private boolean mAutoIP;
    //控制器作为客户端时，目标服务器的域名   
    private String mServerAddr;

    /**
     * 设置控制器的MAC地址，MAC地址有一定的规则，不建议修改。 MAC地址的格式使用 '-' 分隔的6段的十六进制字符串
     * 示例：00-18-06-10-E5-3B
     *
     * @param mac 控制器的MAC地址
     */
    public void SetMAC(String mac) {
        if (!CheckMAC(mac)) {
            return;
        }
        mMAC = mac;
    }

    /**
     * 检查MAC地址是否符合格式。
     *
     * @param mac MAC地址
     * @return true--符合规则，false--不符合
     */
    public static boolean CheckMAC(String mac) {
        if (StringUtil.IsNullOrEmpty(mac)) {
            return false;
        }
        String[] macList = mac.split("-");
        int iLen = macList.length;
        if (iLen != 6) {
            return false;
        }
        try {
            int iValue;
            for (int i = 0; i < iLen; i++) {
                iValue = Integer.parseInt(macList[i],16);
                if (iValue < 0 || iValue > 255) {
                    return false;
                }
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    /**
     * 获取控制器MAC地址
     *
     * @return 控制器MAC地址
     */
    public String GetMAC() {
        return mMAC;
    }

    /**
     * 设置控制器的IPv4地址，IPv4地址分4个段落，格式 192.168.1.150
     *
     * @param ip 控制器的IP地址
     */
    public void SetIP(String ip) {
        if (!CheckIP(ip)) {
            return;
        }

        mIP = ip;
    }

    /**
     * 检查IPv4地址是否符合格式。
     *
     * @param ip IPv4地址
     */
    public static boolean CheckIP(String ip) {
        if (StringUtil.IsNullOrEmpty(ip)) {
            return false;
        }
        String[] ipList = ip.split("\\.");
        int iLen = ipList.length;
        if (iLen != 4) {
            return false;
        }
        try {
            int iValue;
            for (int i = 0; i < iLen; i++) {
                iValue = Integer.parseInt(ipList[i]);
                if (iValue < 0 || iValue > 255) {
                    return false;
                }
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    /**
     * 获取控制器的IP地址。
     *
     * @return 控制器的IP地址
     */
    public String GetIP() {
        return mIP;
    }

    /**
     * 设置子网掩码，IPv4版本
     *
     * @param mask 控制器的子网掩码，格式：255.255.255.0
     */
    public void SetIPMask(String mask) {
        if (!CheckIP(mask)) {
            return;
        }

        mIPMask = mask;

    }

    /**
     * 获取子网掩码
     *
     * @return 子网掩码
     */
    public String GetIPMask() {
        return mIPMask;
    }

    /**
     * 设置网关，IPv4版本
     *
     * @param gateway 控制器的IP网关，格式：192.168.1.1
     */
    public void SetIPGateway(String gateway) {
        if (!CheckIP(gateway)) {
            return;
        }

        mIPGateway = gateway;
    }

    /**
     * 获取网关
     *
     * @return 网关
     */
    public String GetIPGateway() {
        return mIPGateway;
    }

    /**
     * 网络域名解析服务器 DNS 的地址。IPv4 地址
     *
     * @param dns DNS的IP地址，示例：233.5.5.5
     */
    public void SetDNS(String dns) {
        if (!CheckIP(dns)) {
            return;
        }
        mDNS = dns;
    }

    /**
     * 获取DNS服务器IP
     *
     * @return DNS服务器IP
     */
    public String GetDNS() {
        return mDNS;
    }

    /**
     * 备用的网络域名解析服务器 DNS 的地址。IPv4 地址
     *
     * @param dns DNS的IP地址，示例：233.6.6.6
     */
    public void SetDNSBackup(String dns) {
        if (!CheckIP(dns)) {
            return;
        }
        mDNSBackup = dns;
    }

    /**
     * 获取DNS服务器IP
     *
     * @return DNS服务器IP
     */
    public String GetDNSBackup() {
        return mDNSBackup;
    }

    /**
     * 控制器网络协议类型,决定控制器TCP网络工作模式
     *
     * 1--TCP client （控制器就是 Client）; 2--TCP Server（控制器就是 Server）;
     *
     * @param ProtocolType
     */
    public void SetProtocolType(short ProtocolType) {
        if (!UInt32Util.CheckNum(ProtocolType, 1, 2)) {
            return;
        }
        mProtocolType = ProtocolType;
    }

    /**
     * 获取控制器网络工作模式
     *
     * @return 网络工作模式
     */
    public short GetProtocolType() {
        return mProtocolType;
    }

    /**
     * 控制器使用的TCP端口
     *
     * @param port TCP端口 1-65535
     */
    public void SetTCPPort(int port) {
        if (!UInt32Util.CheckNum(port, 1, 65535)) {
            return;
        }
        mTCPPort = port;

    }

    /**
     * 控制器使用的TCP端口
     *
     * @return
     */
    public int GetTCPPort() {
        return mTCPPort;
    }

    /**
     * 控制器使用的UDP端口
     *
     * @param port TCP端口 1-65535
     */
    public void SetUDPPort(int port) {
        if (!UInt32Util.CheckNum(port, 1, 65535)) {
            return;
        }
        mUDPPort = port;

    }

    /**
     * 控制器使用的UDP端口
     *
     * @return
     */
    public int GetUDPPort() {
        return mUDPPort;
    }

    /**
     * 控制器作为客户端时，目标服务器的端口
     *
     * @param port 端口 1-65535
     */
    public void SetServerPort(int port) {
        if (!UInt32Util.CheckNum(port, 1, 65535)) {
            return;
        }
        mServerPort = port;

    }

    /**
     * 控制器作为客户端时，目标服务器的端口
     *
     * @return
     */
    public int GetServerPort() {
        return mServerPort;
    }

    /**
     * 控制器作为客户端时，目标服务器的IP,IPv4版本
     *
     * @param ip IPv4版本，示例：192.168.1.30
     */
    public void SetServerIP(String ip) {
        if (!CheckIP(ip)) {
            return;
        }
        mServerIP = ip;
    }

    /**
     * 控制器作为客户端时，目标服务器的IP,IPv4版本
     *
     * @return
     */
    public String GetServerIP() {
        return mServerIP;
    }

    /**
     * 自动获得IP
     */
    public void SetAutoIP(boolean auto) {
        mAutoIP = auto;
    }

    /**
     * 自动获得IP
     */
    public boolean GetAutoIP() {
        return mAutoIP;
    }

    /**
     * 控制器作为客户端时，目标服务器的域名，示例：www.123.cn
     *
     * @param server 域名由ascii字符组成，长度不得超过 99个字节。
     */
    public void SetServerAddr(String server) {
        if (StringUtil.IsNullOrEmpty(server)) {

            mServerAddr = null;
        } else {
            if (server.length() > 99) {
                mServerAddr = server.substring(0, 99);
            } else {
                mServerAddr = server;
            }
        }
    }

    /**
     * 控制器作为客户端时，目标服务器的域名
     *
     * @return
     */
    public String GetServerAddr() {
        return mServerAddr;
    }
}

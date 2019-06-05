/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Net.PC15.FC8800.Packet;

import Net.PC15.Packet.INPacket;
import Net.PC15.Packet.INPacketModel;
import Net.PC15.Util.ByteUtil;
import io.netty.buffer.ByteBuf;
import Net.PC15.Util.UInt32Util;
import Net.PC15.Util.StringUtil;

/**
 *
 * @author 赖金杰
 */
public class FC8800PacketCompile implements INPacket {

    private FC8800PacketModel _Packet = null;
    private ByteBuf _PacketData;
    private long _CheckSum = 0;

    /**
     * 创建一个空的命令编译类，后续需要调用 SetPacket 方法计算命令检验和。
     */
    public FC8800PacketCompile() {
    }

    /**
     * 根据SN和密码，命令参数创建一个命令包，并计算检验和。
     *
     * @param sn
     * @param pwd
     * @param iCmdType
     * @param iCmdIndex
     * @param iCmdPar
     */
    public FC8800PacketCompile(String sn, long pwd, short iCmdType, short iCmdIndex, short iCmdPar) {
        long lSource = UInt32Util.GetRndNum();//获取一个随机数
        CreateFrame(sn, pwd, lSource, iCmdType, iCmdIndex, iCmdPar, 0, null);
    }

    /**
     * 根据SN和密码，命令参数，数据长度和数据缓冲，创建一个Packet，并计算校验和
     *
     * @param sn
     * @param pwd
     * @param iCmdType
     * @param iCmdIndex
     * @param iCmdPar
     * @param iDataLen
     * @param Databuf
     */
    public FC8800PacketCompile(String sn, long pwd, short iCmdType, short iCmdIndex, short iCmdPar, int iDataLen, ByteBuf Databuf) {
        long lSource = UInt32Util.GetRndNum();//获取一个随机数
        CreateFrame(sn, pwd, lSource, iCmdType, iCmdIndex, iCmdPar, iDataLen, Databuf);
    }

    /**
     * 根据SN和密码、信息代码、命令、命令数据，创建一个Packet并计算校验和。
     *
     * @param sn
     * @param pwd
     * @param code
     * @param iCmdType
     * @param iCmdIndex
     * @param iCmdPar
     * @param iDataLen
     * @param Databuf
     */
    public FC8800PacketCompile(String sn, long pwd, long code, short iCmdType, short iCmdIndex, short iCmdPar, int iDataLen, ByteBuf Databuf) {
        CreateFrame(sn, pwd, code, iCmdType, iCmdIndex, iCmdPar, iDataLen, Databuf);
    }

    /**
     * 根据SN和密码、信息代码、命令、命令数据，创建一个Packet并计算校验和。
     *
     * @param sn
     * @param pwd
     * @param code
     * @param iCmdType
     * @param iCmdIndex
     * @param iCmdPar
     * @param iDataLen
     * @param Databuf
     */
    private void CreateFrame(String sn, long pwd, long code, short iCmdType, short iCmdIndex, short iCmdPar, int iDataLen, ByteBuf Databuf) {
        if (_Packet != null) {
            _Packet.Release();
        }

        _Packet = new FC8800PacketModel();
        _Packet.SetSN(sn);
        _Packet.SetPassword(pwd);
        _Packet.SetCode(code);
        _Packet.SetCmdType(iCmdType);
        _Packet.SetCmdIndex(iCmdIndex);
        _Packet.SetCmdPar(iCmdPar);
        _Packet.SetDataLen(iDataLen);
        if (iDataLen > 0) {
            _Packet.SetDatabuff(Databuf);
        }
        Compile();
    }

    /**
     *
     */
    public void Compile() {
        if (_PacketData != null) {
            if (_PacketData.refCnt() > 0) {
                _PacketData.release(); //释放    
            }
        }
        long iLen = 34L + _Packet.GetDataLen();
        iLen = iLen * 2;//防止出现转译码

        _PacketData = ByteUtil.ALLOCATOR.buffer((int) iLen);

        _PacketData.writeByte(126);//0x7E -- 命令头
        _CheckSum = 0;
        //SN
        Push(StringUtil.FillString(_Packet.GetSN(), 16, "F").getBytes());
        //通讯密码
        Push(UInt32Util.UINT32ToByteBuf(_Packet.GetPassword()));
        //信息代码
        Push(UInt32Util.UINT32ToByteBuf(_Packet.GetCode()));
        //命令类型
        Push(_Packet.GetCmdType());
        //命令分类
        Push(_Packet.GetCmdIndex());
        //命令参数
        Push(_Packet.GetCmdPar());
        //命令长度
        Push(UInt32Util.UINT32ToByteBuf(_Packet.GetDataLen()));
        if (_Packet.GetDataLen() > 0) {
            Push(_Packet.GetDatabuff(), false);
        }
        short sum = (short) (_CheckSum & 0x00000000000000FFL);
        _Packet.SetPacketCheck(sum);
        //计算校验和
        Push(sum);
        _CheckSum = sum;
        //_PacketData.writeByte((int) sum);//校验和

        _PacketData.writeByte(126);//0x7E -- 命令尾
    }

    //将一个字节加入缓冲
    private void Push(int iByte) {
        _CheckSum += iByte;
        if (iByte == 126) { //0x7E == 0x7F 01 
            _PacketData.writeByte(127); // 0x7F
            _PacketData.writeByte(1);//0x01
            return;
        }
        if (iByte == 127) { //0x7F == 0x7F 02 
            _PacketData.writeByte(127); // 0x7F
            _PacketData.writeByte(2);//0x02
            return;
        }
        _PacketData.writeByte(iByte);

    }

    /**
     * 将一个字节数组加入到缓冲区
     *
     * @param buf
     */
    private void Push(byte[] buf) {
        for (byte b : buf) {
            int num = ByteUtil.uByte(b);
            Push(num);
        }
    }

    /**
     * 将ByteBuf加入缓冲区，使用后释放此ByteBuf
     *
     * @param buf
     */
    private void Push(ByteBuf buf) {
        Push(buf, true);
    }

    /**
     * 将ByteBuf加入缓冲区，可设定使用后是否释放 ByteBuf
     *
     * @param buf
     * @param release
     */
    private void Push(ByteBuf buf, boolean release) {
        buf.forEachByte((byte value) -> {
            Push(ByteUtil.uByte(value));
            return true;
        });
        if (release) {
            buf.release();
        }
        buf = null;
    }

    /**
     * 获取命令实体类
     *
     * @return
     */
    @Override
    public INPacketModel GetPacket() {
        return _Packet;
    }

    /**
     * 设置命令实体类
     *
     * @param packet
     */
    @Override
    public void SetPacket(INPacketModel packet) {
        /*if (_PacketData != null) {
            _PacketData.release(); //释放
        }*/
        _Packet = (FC8800PacketModel) packet;
        if (_Packet == null) {
            Release();
        }
        Compile();
    }

    @Override
    public ByteBuf GetPacketData() {
        return _PacketData;
    }

    @Override
    public void Release() {
        if (_PacketData != null) {
            if (_PacketData.refCnt() > 0) {
                _PacketData.release(); //释放    
            }

            _PacketData = null;
        }

        if (_Packet != null) {
            _Packet.Release();
            _Packet = null;
        }

    }

}

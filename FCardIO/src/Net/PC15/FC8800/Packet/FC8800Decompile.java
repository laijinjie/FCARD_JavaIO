/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Net.PC15.FC8800.Packet;

import Net.PC15.Packet.INPacketDecompile;
import Net.PC15.Packet.INPacketModel;
import Net.PC15.Util.ByteUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.UnpooledByteBufAllocator;
import java.util.ArrayList;

/**
 * FC8800系列控制器的命令包解析
 *
 * @author 赖金杰
 */
public class FC8800Decompile implements INPacketDecompile {

    private int _Step = 0;//接收数据的步骤
    private ByteBuf _Buf;
    private boolean _Translate = false;//接收到转移码
    private FC8800PacketModel _Packet = null;
    private long _Checksum = 0;
    private final byte[] _SNBytes;
    private ByteBuf _DataBuf;
    private int _DataLen;

    public static final ByteBufAllocator DEFAULT_POOLED = ByteUtil.ALLOCATOR;

    public FC8800Decompile() {
        _Buf = DEFAULT_POOLED.buffer(32);
        _SNBytes = new byte[16];
        ClearBuf();
    }

    @Override
    public boolean Decompile(ByteBuf bDataBuf, ArrayList<INPacketModel> oRetPack) {
        bDataBuf.markReaderIndex();
        boolean bDecompile = false;
        boolean ret = false;
        int iBufLen = bDataBuf.readableBytes();
        for (int i = 0; i < iBufLen; i++) {
            short num = bDataBuf.readUnsignedByte();
            ret = DecompileByte(num);
            if (ret) {
                bDecompile = true;
                oRetPack.add(_Packet);
                _Packet = null;
                ClearBuf();
            }
        }

        bDataBuf.resetReaderIndex();
        return bDecompile;
    }

    /**
     * 对字节进行转译
     *
     * @param value
     * @return
     */
    private boolean DecompileByte(short value) {
        switch (value) {
            case 126://0x7e
                ClearBuf();
                break;
            case 127://0x7f
                _Translate = true;
                break;
            default:
                if (_Translate) {
                    switch (value) {
                        case 1:
                            value = 126;//7f 01=7e
                            break;
                        case 2:
                            value = 127;//7f 02=7f
                            break;
                        default:
                            ClearBuf();
                            return false;
                    }
                    _Translate = false;
                    return DecompileStep(value);
                } else {
                    return DecompileStep(value);
                }
        }
        return false;
    }

    /**
     * 将数据加入到缓冲
     *
     * @param value
     */
    private void push(short value) {
        _Checksum += value;
        _Buf.writeByte(value);
    }

    /**
     * 处理转以后的字节
     *
     * @param value
     * @return
     */
    private boolean DecompileStep(short value) {
        if (_Packet == null) {
            ClearBuf();
        }
        //开始按格式进行计算
        switch (_Step) {
            case 0://信息代码
                if (_Buf.readableBytes() != 4) //未收到4个字节，继续接收
                {
                    push(value);

                    if (_Buf.readableBytes() == 4) {
                        _Packet.SetCode(_Buf.readUnsignedInt());	//保存
                        _Buf.clear();
                        _Step++;//进行到下一步
                    }
                } else {
                    ClearBuf();
                }
                break;
            case 1://SN
                if (_Buf.readableBytes() != 16) //未收到16个字节，继续接收
                {
                    push(value);

                    if (_Buf.readableBytes() == 16) {

                        try {
                            _Buf.readBytes(_SNBytes);
                            _Packet.SetSN(new String(_SNBytes, "GB2312"));	//保存	
                            _Buf.clear();
                            _Step++;//进行到下一步
                        } catch (Exception e) {
                            ClearBuf();
                        }
                    }
                } else {
                    ClearBuf();;//溢出过边界
                }
                break;
            case 2://通讯密码
                if (_Buf.readableBytes() != 4) //未收到4个字节，继续接收
                {
                    push(value);

                    if (_Buf.readableBytes() == 4) {
                        _Packet.SetPassword(_Buf.readUnsignedInt());
                        _Buf.clear();
                        _Step++;//进行到下一步
                    }
                } else {
                    ClearBuf();//溢出过边界
                }
                break;
            case 3://命令类型
                _Checksum += value;
                _Packet.SetCmdType(value);	//保存
                _Step++;//进行到下一步
                break;
            case 4://命令索引
                _Checksum += value;
                _Packet.SetCmdIndex(value);	//保存
                _Step++;//进行到下一步
                break;
            case 5://命令参数
                _Checksum += value;
                _Packet.SetCmdPar(value);	//保存
                _Step++;//进行到下一步
                break;
            case 6://数据长度
                if (_Buf.readableBytes() != 4) //未收到4个字节，继续接收
                {
                    push(value);

                    if (_Buf.readableBytes() == 4) {
                        _DataLen = (int) _Buf.readUnsignedInt();
                        if (_DataLen > 2046 || _DataLen < 0) {
                            ClearBuf();
                            return false;
                        }
                        _Packet.SetDataLen(_DataLen);	//保存		
                        _Buf.clear();
                        _Step++;//进行到下一步

                        if (_DataLen != 0) {
                            _DataBuf = DEFAULT_POOLED.buffer((int) _DataLen);
                            _Packet.SetDatabuff(_DataBuf);
                        } else {
                            //跳过存储数据步骤
                            _Step++;
                        }
                    }
                } else {
                    ClearBuf();//溢出过边界
                }
                break;
            case 7://命令数据
                if (_DataBuf.readableBytes() != _DataLen) //未收到指定的字节，继续接收
                {

                    
                    _Checksum += value;
                    _DataBuf.writeByte(value);
                    if (_DataBuf.readableBytes() == _DataLen) {

                        //System.out.println(ByteBufUtil.hexDump(_DataBuf));
                        _Packet.SetDatabuff(_DataBuf);
                        _Step++;//进行到下一步
                    }
                } else {
                    ClearBuf();//溢出过边界
                }
                break;
            case 8://计算检验和
                _Step++;//进行到下一步
                _Checksum = _Checksum & 0x000000FFL;
                _Packet.SetPacketCheck(value);
                if (_Checksum == value) {
                    return true;//检验和通过
                } else {
                    return false;
                }
            default:
                ClearBuf();
        }
        return false;
    }

    @Override
    public void ClearBuf() {
        _Buf.clear();
        _Step = 0;
        _Translate = false;
        if (_Packet != null) {
            _Packet.Release();
            _Packet = null;
        }
        _Packet = new FC8800PacketModel();

        _Checksum = 0;
        _DataBuf = null;
        _DataLen = 0;
    }

    @Override
    public void Release() {
        if (_Buf != null) {
            _Buf.release();
            _Buf = null;
        }

        _Step = 0;
        _Translate = false;
        if (_Packet != null) {
            _Packet.Release();
            _Packet = null;
        }

        _Checksum = 0;
        _DataBuf = null;
        _DataLen = 0;

    }

}

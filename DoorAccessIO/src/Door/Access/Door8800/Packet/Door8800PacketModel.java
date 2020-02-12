/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Door.Access.Door8800.Packet;

import Door.Access.Packet.INPacketModel;
import Door.Access.Util.ByteUtil;
import io.netty.buffer.ByteBuf;
import Door.Access.Util.StringUtil;
import Door.Access.Util.UInt32Util;

/**
 * Door8800 命令格式解析
 *
 * @author 赖金杰
 */
public class Door8800PacketModel implements INPacketModel {

    private String _SN; //控制器的SN
    private long _Password;//通讯密码
    private long _Code;//信息代码
    private short _CmdType;//命令类型
    private short _CmdIndex;//命令索引
    private short _CmdPar;//命令参数
    private long _DataLen;//数据部分长度
    private ByteBuf _CmdData;//数据部分
    private short _Check;//校验和

    public Door8800PacketModel() {
        _Code = UInt32Util.GetRndNum();
        _Password = UInt32Util.UINT32_MAX;
    }

    /**
     * 返回控制器的SN
     *
     * @return
     */
    public String GetSN() {
        return _SN;
    }

    /**
     * 设置控制器的SN，SN由16位Ascii字符组成，示例：AB-8810A00000001
     *
     * @param sn
     */
    public void SetSN(String sn) {
        if (StringUtil.IsNullOrEmpty(sn)) {
            return;
        }
        if (!StringUtil.IsAscii(sn)) {
            return;
        }
        if (sn.length() != 16) {
            return;
        }
        _SN = sn;
    }

    /**
     * 获取通讯密码
     *
     * @return
     */
    public long GetPassword() {
        return _Password;
    }

    /**
     * 设置通讯密码，通讯密码为四字节数值。
     *
     * @param password
     */
    public void SetPassword(long password) {
        if (!UInt32Util.Check(password)) {
            return;
        }
        _Password = password;
    }

    /**
     * 获取命令代码--信息代码，命令索引号
     *
     * @return
     */
    public long GetCode() {
        return _Code;
    }

    /**
     * 设置命令代码--信息代码，命令索引号
     *
     * @param code
     */
    public void SetCode(long code) {
        if (!UInt32Util.Check(code)) {
            return;
        }
        _Code = code;
    }

    /**
     * 设置命令类型 取值范围 0-255
     *
     * @param iData
     */
    public void SetCmdType(short iData) {
        if (!ByteUtil.Check(iData)) {
            return;
        }
        _CmdType = iData;
    }

    /**
     * 获取命令类型
     *
     * @return
     */
    public short GetCmdType() {
        return _CmdType;
    }

    /**
     * 设置命令索引 取值范围 0-255
     *
     * @param iData
     */
    public void SetCmdIndex(short iData) {
        if (!ByteUtil.Check(iData)) {
            return;
        }
        _CmdIndex = iData;
    }

    /**
     * 获取命令索引
     *
     * @return
     */
    public short GetCmdIndex() {
        return _CmdIndex;
    }

    /**
     * 设置命令参数 取值范围 0-255
     *
     * @param iData
     */
    public void SetCmdPar(short iData) {
        if (!ByteUtil.Check(iData)) {
            return;
        }
        _CmdPar = iData;
    }

    /**
     * 获取命令参数
     *
     * @return
     */
    public short GetCmdPar() {
        return _CmdPar;
    }

    /**
     * 数据长度 取值范围 0-65535
     *
     * @param iLen 取值范围 0-65535
     */
    public void SetDataLen(long iLen) {
        if (!UInt32Util.Check(iLen)) {
            return;
        }
        _DataLen = iLen;
    }

    /**
     * 获取数据长度
     *
     * @return
     */
    public long GetDataLen() {
        return _DataLen;
    }

    /**
     * 设置命令数据
     *
     * @param data
     */
    public void SetDatabuff(ByteBuf data) {
        _CmdData = data;
    }

    /**
     * 获取命令数据
     *
     * @return
     */
    public ByteBuf GetDatabuff() {
        return _CmdData;
    }

    /**
     * 设置校验和 取值范围 0-255
     *
     * @param iData
     */
    public void SetPacketCheck(short iData) {
        if (!ByteUtil.Check(iData)) {
            return;
        }
        _Check = iData;
    }

    /**
     * 获取校验和
     *
     * @return
     */
    public int GetPacketCheck() {
        return _Check;
    }

    /**
     * 释放数据缓冲区
     */
    @Override
    public void Release() {
        if (_CmdData != null) {
            if (_CmdData.refCnt() > 0) {
                _CmdData.release();
            }
            _CmdData = null;
        }
    }
}

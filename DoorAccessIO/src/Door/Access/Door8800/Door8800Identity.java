/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Door.Access.Door8800;

import Door.Access.Connector.E_ControllerType;
import Door.Access.Command.INIdentity;
import Door.Access.Util.StringUtil;

/**
 * 指示控制器的身份，例如门禁控制器
 *
 * @author 赖金杰
 */
public class Door8800Identity implements INIdentity {

    private String mPassword; //通讯密码 十六进制表示 4-8个字节
    private String mSN;//SN 16位
    private E_ControllerType mType;

    public Door8800Identity(String sn, String pwd, E_ControllerType type) {
        SetSN(sn);
        SetPassword(pwd);
        SetType(type);
    }

    @Override
    public String GetPassword() {
        return mPassword;
    }

    @Override
    public String GetIdentity() {
        return mSN;
    }

    @Override
    public E_ControllerType GetIdentityType() {
        return mType;
    }

    /**
     * 设置控制器类型
     *
     * @param type 类型枚举
     */
    public void SetType(E_ControllerType type) {
        mType = type;
    }

    /**
     * 设置用于验证的密码
     *
     * @param pwd 密码 8-16位十六进制
     */
    public void SetPassword(String pwd) {
        if (StringUtil.IsNullOrEmpty(pwd) || !StringUtil.IsHex(pwd)
                || pwd.length() > 16 || pwd.length() < 8) {
            throw new IllegalArgumentException(
                    "pwd 不是十六进制或长度超出限制.");
        }

        mPassword = pwd;
    }

    /**
     * 设置一个字符串，当命令需要时，用于验证身份 此处需要的是控制器SN
     *
     * @param sn 控制器SN 16字节的ASCII字符串
     *
     */
    public void SetSN(String sn) {
        if (StringUtil.IsNullOrEmpty(sn) || !StringUtil.IsAscii(sn) || sn.length() != 16) {
            throw new IllegalArgumentException(
                    "sn 长度不正确或不是有效的Ascii字符.");
        }
        mSN = sn;
    }

    /**
     * 获取控制器SN
     *
     * @return 返回控制器SN
     */
    public String GetSN() {
        return mSN;
    }

    /**
     * 用来和对方进行对比
     *
     * @param dec
     * @return
     */
    @Override
    public boolean equals(INIdentity dec) {
        if (dec == null) {
            return false;
        }
        
        String clsName = this.getClass().getCanonicalName();
        String decName = dec.getClass().getCanonicalName();
        if (!clsName.equals(decName)) {
            return false;
        }
        return GetIdentity().equals(dec.GetIdentity());
    }
}

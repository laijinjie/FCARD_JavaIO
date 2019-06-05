/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Net.PC15.FC8800.Command.System.Parameter;

import Net.PC15.Command.CommandDetail;
import Net.PC15.Command.CommandParameter;
import Net.PC15.Util.StringUtil;

/**
 * 修改控制器通讯密码命令的参数
 *
 * @author 赖金杰
 */
public class WriteConnectPassword_Parameter extends CommandParameter {

    private String _PWD;

    public WriteConnectPassword_Parameter(CommandDetail detail, String pwd) {
        super(detail);
        SetPassword(pwd);
    }

    /**
     * 获取控制器通讯密码
     *
     * @return 通讯密码
     */
    public String GetPassword() {
        return _PWD;
    }

    /**
     * 控制器通讯密码为十六进制字符串，4个字节。示例：12345678
     *
     * @param password 通讯密码为十六进制字符串，4个字节。示例：12345678
     */
    public void SetPassword(String password) {
        if (StringUtil.IsNullOrEmpty(password)) {
            return;
        }
        if (!StringUtil.IsHex(password)) {
            return;
        }
        if (password.length() != 8) {
            return;
        }
        _PWD = password;
    }

}

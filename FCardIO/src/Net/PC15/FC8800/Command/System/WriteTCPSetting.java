/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Net.PC15.FC8800.Command.System;

import Net.PC15.FC8800.Command.Data.TCPDetail;
import Net.PC15.FC8800.Command.FC8800Command;
import Net.PC15.FC8800.Command.System.Parameter.WriteTCPSetting_Parameter;

/**
 * 修改控制器TCP网络参数
 *
 * @author 赖金杰
 */
public class WriteTCPSetting extends FC8800Command {

    public WriteTCPSetting(WriteTCPSetting_Parameter par) {
        _Parameter = par;
        TCPDetail tcp = par.GetTCP();
        if (tcp == null) {
            throw new IllegalArgumentException("tcp is null");
        }

        CreatePacket(1, 6, 1, tcp.GetDataLen(), tcp.GetBytes());
    }

    @Override
    protected void Release0() {
        return;
    }

}

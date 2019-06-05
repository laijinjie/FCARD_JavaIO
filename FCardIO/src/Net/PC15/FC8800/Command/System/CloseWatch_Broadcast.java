/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Net.PC15.FC8800.Command.System;

import Net.PC15.Command.CommandParameter;
import Net.PC15.FC8800.Command.FC8800Command;

/**
 * 广播关闭监控
 * @author 赖金杰
 */
public class CloseWatch_Broadcast extends FC8800Command {

    public CloseWatch_Broadcast(CommandParameter par) {
        _Parameter = par;
        CreatePacket(1, 0xB, 0x11);
        _IsWaitResponse = false;

    }

    @Override
    protected void Release0() {
        return;
    }
    
}

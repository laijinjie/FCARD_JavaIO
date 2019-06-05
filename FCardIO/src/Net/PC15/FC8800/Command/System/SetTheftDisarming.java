/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Net.PC15.FC8800.Command.System;

import Net.PC15.Command.CommandParameter;
import Net.PC15.FC8800.Command.FC8800Command;

/**
 * 防盗报警撤防
 * @author 赖金杰
 */
public class SetTheftDisarming extends FC8800Command {

    public SetTheftDisarming(CommandParameter par) {
        _Parameter = par;
        CreatePacket(1, 0x11, 2);
    }

    @Override
    protected void Release0() {
        return;
    }
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Door.Access.Door8800.Command.System;

import Door.Access.Command.CommandParameter;
import Door.Access.Door8800.Command.Door8800Command;

/**
 *防盗报警布防
 * @author 赖金杰
 */
public class SetTheftFortify extends Door8800Command {

    public SetTheftFortify(CommandParameter par) {
        _Parameter = par;
        CreatePacket(1, 0x11, 1);
    }

    @Override
    protected void Release0() {
        return;
    }
    
}

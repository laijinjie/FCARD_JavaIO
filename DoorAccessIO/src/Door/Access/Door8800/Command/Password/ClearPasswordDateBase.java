/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Door.Access.Door8800.Command.Password;

import Door.Access.Command.CommandParameter;
import Door.Access.Door8800.Command.Door8800Command;

/**
 *清空所有开门密码
 * @author F
 */
public class ClearPasswordDateBase extends Door8800Command {
      public ClearPasswordDateBase(CommandParameter par){
        _Parameter=par;
        CreatePacket(0x05, 0x02,0x00);
    }

    @Override
    protected void Release0() {
       
    }
}

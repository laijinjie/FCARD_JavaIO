/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Face.Door;

import Door.Access.Command.CommandParameter;
import Door.Access.Door8800.Command.Door8800Command;

/**
 *远程关门
 * @author F
 */
public class CloseDoor extends Door8800Command{
/**
 * 远程关门
 * @param par 命令参数
 */
    public CloseDoor(CommandParameter par){
        _Parameter=par;
        CreatePacket(0x03, 0x03,0x01);
    }
    @Override
    protected void Release0() {
       return;
    }
    
}

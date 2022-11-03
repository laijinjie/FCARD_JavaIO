/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Face.Door;

import Door.Access.Command.CommandParameter;
import Door.Access.Door8800.Command.Door8800Command;

/**
 *锁定门
 * @author F
 */
public class LockDoor extends Door8800Command {
/**
 * 锁定门
 * @param par 命令参数
 */
    public LockDoor(CommandParameter par){
        _Parameter=par;
        CreatePacket(0x03, 0x04);
    }
    @Override
    protected void Release0() {
        return;
    }
    
}

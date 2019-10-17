/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Net.PC15.FC8800.Command.TimeGroup;

import Net.PC15.Command.CommandParameter;
import Net.PC15.FC8800.Command.FC8800Command;
import Net.PC15.Util.ByteUtil;
import io.netty.buffer.ByteBuf;

/**
 * 清空所有开门时段
 * @author 徐铭康
 */
public class ClearTimeGroup extends FC8800Command {

    public ClearTimeGroup(CommandParameter par){
        _Parameter = par;
        CreatePacket(0x06, 0x01);
    }
    @Override
    protected void Release0() {
        
    }
    
    
}

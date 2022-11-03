/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Face.System;

import Door.Access.Command.CommandParameter;
import Door.Access.Door8800.Command.Door8800Command;

/**
 *读取补光灯模式
 * @author F
 */
public class ReadFaceLEDMode extends Door8800Command {

    /**
     * 读取补光灯模式
     * @param parameter 
     */
    public ReadFaceLEDMode(CommandParameter parameter) {
        _Parameter = parameter;
        CreatePacket(1, 39, 1);
    }

    @Override
    protected void Release0() {

    }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Face.System;

import Door.Access.Door8800.Command.Door8800Command;
import Door.Access.Util.ByteUtil;
import Face.System.Parameter.WriteFaceLEDMode_Parameter;
import io.netty.buffer.ByteBuf;

/**
 *设置补光灯模式
 * @author F
 */
public class WriteFaceLEDMode extends Door8800Command {
/**
 * 补光灯模式
 * @param parameter 
 */
    public WriteFaceLEDMode(WriteFaceLEDMode_Parameter parameter) {
        _Parameter = parameter;
        ByteBuf buf = ByteUtil.ALLOCATOR.buffer(1);
        buf.writeByte(parameter.LEDMode);
        CreatePacket(1, 39, 0, 1, buf);
    }

    @Override
    protected void Release0() {

    }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Face.System;

import Door.Access.Door8800.Command.Door8800Command;
import Door.Access.Util.ByteUtil;
import Face.System.Parameter.WriteFaceBodyTemperaturePar_Parameter;
import io.netty.buffer.ByteBuf;

/**
 * 设置体温检测开关及体温格式
 *
 * @author F
 */
public class WriteFaceBodyTemperaturePar extends Door8800Command {

    /**
     * 设置体温检测开关及体温格式
     *
     * @param parameter
     */
    public WriteFaceBodyTemperaturePar(WriteFaceBodyTemperaturePar_Parameter parameter) {
        _Parameter = parameter;
        ByteBuf buf = ByteUtil.ALLOCATOR.buffer(2);
        if (parameter.BodyTemperaturePar == 0) {
            buf.writeByte(0);
            buf.writeByte(1);
        } else {
            buf.writeByte(1);
            buf.writeByte(parameter.BodyTemperaturePar);
        }
        CreatePacket(1, 41, 0, 2, buf);
    }

    @Override
    protected void Release0() {

    }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Face.System;

import Door.Access.Door8800.Command.Door8800Command;
import Door.Access.Util.ByteUtil;
import Face.System.Parameter.WriteFaceMouthmufflePar_Parameter;
import io.netty.buffer.ByteBuf;

/**
 *设置口罩识别开关
 * @author F
 */
public class WriteFaceMouthmufflePar extends Door8800Command {

    public WriteFaceMouthmufflePar(WriteFaceMouthmufflePar_Parameter par) {
        _Parameter=par;
        ByteBuf buf = ByteUtil.ALLOCATOR.buffer(1);
        buf.writeByte(par.Mouthmuffle);
        CreatePacket(1,40,0,1,buf);
    }

    @Override
    protected void Release0() {

    }

}

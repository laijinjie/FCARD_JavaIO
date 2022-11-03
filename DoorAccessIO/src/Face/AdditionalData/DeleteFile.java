/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Face.AdditionalData;

import Door.Access.Door8800.Command.Door8800Command;
import Door.Access.Util.ByteUtil;
import Face.AdditionalData.Parameter.DeleteFile_Parameter;
import io.netty.buffer.ByteBuf;

/**
 *
 * @author FCARD
 */
public class DeleteFile extends Door8800Command {

    public DeleteFile(DeleteFile_Parameter parameter) {
        _Parameter = parameter;
        ByteBuf buf = ByteUtil.ALLOCATOR.buffer(parameter.GetDataLen());
        parameter.getBytes(buf);
        CreatePacket(0x0b, 0x06, 0x00, parameter.GetDataLen(), buf);
    }

    @Override
    protected void Release0() {

    }

}

package Face.System;

import Door.Access.Door8800.Command.Door8800Command;
import Face.System.Parameter.WriteOEM_Parameter;

import java.io.UnsupportedEncodingException;

/**
 * 写入OEM信息
 */
public class WriteOEM extends Door8800Command {
    /**
     * 写入OEM信息
     * @param parameter oem参数
     * @throws UnsupportedEncodingException 写入错误
     */
    public WriteOEM(WriteOEM_Parameter parameter) throws UnsupportedEncodingException {
        _Parameter = parameter;
        CreatePacket(0x01, 0x1E, 0x00, parameter.getDataLen(), parameter.oemDetail.getBytes());
    }

    @Override
    protected void Release0() {

    }
}

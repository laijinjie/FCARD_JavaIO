package Face.System;

import Door.Access.Door8800.Command.Door8800Command;
import Face.System.Parameter.WriteWiegandOutput_Parameter;

/**
 * 写入韦根参数
 */
public class WriteWiegandOutput extends Door8800Command {
    /**
     * 写入韦根参数
     * @param parameter 写入参数
     */
    public  WriteWiegandOutput(WriteWiegandOutput_Parameter parameter){
        _Parameter=parameter;
        if(!parameter.checkedParameter()){
            throw new IllegalArgumentException("WriteWiegandOutput_Parameter error");
        }
        CreatePacket(0x01, 0x1c, 0x02,parameter.GetDataLen(),parameter.GetBytes());
    }
    @Override
    protected void Release0() {

    }
}

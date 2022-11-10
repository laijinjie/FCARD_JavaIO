/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Face.System;

import Door.Access.Door8800.Command.Door8800Command;
import Face.System.Parameter.WriteOfflineRecordPush_Parameter;

/**
 *
 * @author kaifa
 */
public class WriteOfflineRecordPush extends Door8800Command {
    /**
     * 写入韦根参数
     * @param parameter 写入参数
     */
    public  WriteOfflineRecordPush(WriteOfflineRecordPush_Parameter parameter){
        _Parameter=parameter;
        if(!parameter.checkedParameter()){
            throw new IllegalArgumentException("WriteOfflineRecordPush_Parameter error");
        }
        CreatePacket(0x01, 0x37, 0x00,parameter.GetDataLen(),parameter.GetBytes());
    }
    @Override
    protected void Release0() {

    }
}

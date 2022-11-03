/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Face.System.Result;

import Door.Access.Command.CommandDetail;
import Door.Access.Command.INCommandResult;
import Face.System.Parameter.WriteFaceBodyTemperaturePar_Parameter;

/**
 * 读取体温检测开关及体温格式 返回结果
 * @author F
 */
public class ReadFaceBodyTemperaturePar_Result extends WriteFaceBodyTemperaturePar_Parameter implements INCommandResult {
/**
 * 读取体温检测开关及体温格式 返回结果
 * @param bodyTemperaturePar 
 */
    public ReadFaceBodyTemperaturePar_Result( int bodyTemperaturePar) {
        super(bodyTemperaturePar);
    }

    @Override
    public void release() {
       
    }
    
}

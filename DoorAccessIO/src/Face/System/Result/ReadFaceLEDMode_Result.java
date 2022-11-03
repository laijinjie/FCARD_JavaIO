/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Face.System.Result;

import Door.Access.Command.CommandDetail;
import Door.Access.Command.INCommandResult;
import Face.System.Parameter.WriteFaceLEDMode_Parameter;

/**
 *读取补光灯模式 返回结果
 * @author F
 */
public class ReadFaceLEDMode_Result extends WriteFaceLEDMode_Parameter implements INCommandResult {
/**
 * 读取补光灯模式 返回结果
 * @param ledMode 
 */
    public ReadFaceLEDMode_Result(int ledMode) {
        super(ledMode);
    }

    @Override
    public void release() {
       
    }

}

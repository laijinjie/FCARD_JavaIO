/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Face.System.Result;

import Door.Access.Command.INCommandResult;
import Face.System.Parameter.WriteFaceMouthmufflePar_Parameter;

/**
 * 读取口罩识别开关返回结果
 *
 * @author F
 */
public class ReadFaceMouthmufflePar_Result extends WriteFaceMouthmufflePar_Parameter implements INCommandResult {

    public ReadFaceMouthmufflePar_Result(int mouthmuffle) {
        super(mouthmuffle);
    }

    @Override
    public void release() {
        
    }
}

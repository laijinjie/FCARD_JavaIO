/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Net.PC15.FC8800.Command.DateTime.Result;

import Net.PC15.Command.INCommandResult;
import java.util.Calendar;

/**
 *
 * @author 赖金杰
 */
public class ReadTime_Result implements INCommandResult{
    /**
     * 控制器的时间日期
     */
    public final Calendar ControllerDate;
    public ReadTime_Result(Calendar t){
        this.ControllerDate = t;
    }
    @Override
    public void release() {
        return;
    }
    
}

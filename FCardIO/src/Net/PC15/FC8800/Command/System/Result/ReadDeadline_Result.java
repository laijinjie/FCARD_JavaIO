/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Net.PC15.FC8800.Command.System.Result;

import Net.PC15.Command.INCommandResult;

/**
 * 读取有效期剩余天数的返回值
 * @author 赖金杰
 */
public class ReadDeadline_Result  implements INCommandResult{
    /**
     * 控制器的剩余天数，天数为0时，刷卡需要刷5下才能开门。
     */
    public final int Deadline;
    public ReadDeadline_Result(int deadline){
        this.Deadline = deadline;
    }
    @Override
    public void release() {
        return;
    }
    
}

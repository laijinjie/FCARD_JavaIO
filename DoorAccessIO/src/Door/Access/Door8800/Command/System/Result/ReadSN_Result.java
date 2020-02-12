/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Door.Access.Door8800.Command.System.Result;

import Door.Access.Command.INCommandResult;

/**
 * 获取SN指令的返回值
 * @author 赖金杰
 */
public class ReadSN_Result implements INCommandResult{
    /**
     * 控制器的SN
     */
    public final String SN;
    public ReadSN_Result(String sn){
        this.SN = sn;
    }
    @Override
    public void release() {
        return;
    }
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Door.Access.Door8800.Command.System.Result;

import Door.Access.Command.INCommandResult;
/**
 * 卡片到期提示参数
 * @author 赖金杰
 */
public class ReadCardPeriodSpeak_Result implements INCommandResult{
    /**
     * 是否启用此功能
     */
    public boolean Use;
    
    @Override
    public void release() {
        return;
    }
    
}

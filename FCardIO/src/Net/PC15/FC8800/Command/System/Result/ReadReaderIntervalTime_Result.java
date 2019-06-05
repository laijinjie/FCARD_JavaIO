/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Net.PC15.FC8800.Command.System.Result;

import Net.PC15.Command.INCommandResult;

/**
 *
 * @author 赖金杰
 */
public class ReadReaderIntervalTime_Result implements INCommandResult{
    /**
     * 读卡间隔时间. <br/>
     * 最大65535秒。0表示无限制
     */
    public int IntervalTime;
    @Override
    public void release() {
        return;
    }
    
}

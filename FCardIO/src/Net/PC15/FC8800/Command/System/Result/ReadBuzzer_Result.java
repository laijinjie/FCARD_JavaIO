/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Net.PC15.FC8800.Command.System.Result;

import Net.PC15.Command.INCommandResult;

/**
 * 主板蜂鸣器参数
 *
 * @author 赖金杰
 */
public class ReadBuzzer_Result implements INCommandResult {

    /**
     * 主板蜂鸣器.<br/>
     * 0不启用，1启用。
     */
    public short Buzzer;

    @Override
    public void release() {
        return;
    }
}

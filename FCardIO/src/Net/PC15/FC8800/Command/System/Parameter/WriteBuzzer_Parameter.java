/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Net.PC15.FC8800.Command.System.Parameter;

import Net.PC15.Command.CommandDetail;
import Net.PC15.Command.CommandParameter;

/**
 * 主板蜂鸣器参数
 *
 * @author 赖金杰
 */
public class WriteBuzzer_Parameter extends CommandParameter {

    /**
     * 主板蜂鸣器.<br/>
     * 0不启用，1启用。
     */
    public short Buzzer;

    public WriteBuzzer_Parameter(CommandDetail detail) {
        super(detail);
    }

}

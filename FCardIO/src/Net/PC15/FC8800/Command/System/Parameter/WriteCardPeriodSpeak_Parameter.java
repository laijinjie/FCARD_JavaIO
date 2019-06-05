/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Net.PC15.FC8800.Command.System.Parameter;

import Net.PC15.Command.CommandDetail;
import Net.PC15.Command.CommandParameter;

/**
 * 卡片到期提示参数
 *
 * @author 赖金杰
 */
public class WriteCardPeriodSpeak_Parameter extends CommandParameter {
    /**
     * 是否启用此功能.
     */
    public boolean Use;

    public WriteCardPeriodSpeak_Parameter(CommandDetail detail) {
        super(detail);
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Net.PC15.FC8800.Command.System.Parameter;

import Net.PC15.Command.CommandDetail;
import Net.PC15.Command.CommandParameter;

/**
 * 防潜回参数
 *
 * @author 赖金杰
 */
public class WriteCheckInOut_Parameter extends CommandParameter {

    /**
     * 防潜回功能参数.<br/>
     * 01--单独每个门检测防潜回；02--整个控制器统一防潜回
     */
    public short Mode;

    public WriteCheckInOut_Parameter(CommandDetail detail) {
        super(detail);
    }

}

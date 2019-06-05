/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Net.PC15.FC8800.Command.DateTime.Parameter;

import Net.PC15.Command.CommandDetail;
import Net.PC15.Command.CommandParameter;
import java.util.Calendar;

/**
 * 写入自定义时间到控制器
 *
 * @author 赖金杰
 */
public class WriteTimeDefine_Parameter extends CommandParameter {

    /**
     * 控制器的时间日期
     */
    public final Calendar ControllerDate;

    public WriteTimeDefine_Parameter(CommandDetail detail, Calendar t) {
        super(detail);
        this.ControllerDate = t;
    }
}

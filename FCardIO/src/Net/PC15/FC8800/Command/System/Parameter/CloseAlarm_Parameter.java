/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Net.PC15.FC8800.Command.System.Parameter;

import Net.PC15.Command.CommandDetail;
import Net.PC15.Command.CommandParameter;
import Net.PC15.FC8800.Command.Data.CloseAlarmType;

/**
 * 解除报警
 *
 * @author 赖金杰
 */
public class CloseAlarm_Parameter extends CommandParameter {

    /**
     * 需要解除报警的门，取值范围：1-4
     */
    public int Door;

    /**
     * 需要解除的报警类型
     */
    public CloseAlarmType Alarm;

    public CloseAlarm_Parameter(CommandDetail detail) {
        super(detail);
        Door = 255;
        Alarm = new CloseAlarmType();
    }

}

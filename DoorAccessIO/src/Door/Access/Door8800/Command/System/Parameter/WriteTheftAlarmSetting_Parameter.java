/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Door.Access.Door8800.Command.System.Parameter;

import Door.Access.Command.CommandDetail;
import Door.Access.Command.CommandParameter;
import Door.Access.Door8800.Command.Data.TheftAlarmSetting;

/**
 * 智能防盗主机参数
 *
 * @author 赖金杰
 */
public class WriteTheftAlarmSetting_Parameter extends CommandParameter {

    public TheftAlarmSetting Setting;

    public WriteTheftAlarmSetting_Parameter(CommandDetail detail) {
        super(detail);
    }

}

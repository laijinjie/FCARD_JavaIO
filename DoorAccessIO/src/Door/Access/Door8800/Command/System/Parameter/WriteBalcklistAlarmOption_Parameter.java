/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Door.Access.Door8800.Command.System.Parameter;

import Door.Access.Command.CommandDetail;
import Door.Access.Command.CommandParameter;

/**
 * 黑名单报警功能开关
 *
 * @author 赖金杰
 */
public class WriteBalcklistAlarmOption_Parameter extends CommandParameter {

    public boolean Use;

    public WriteBalcklistAlarmOption_Parameter(CommandDetail detail) {
        super(detail);
    }

}

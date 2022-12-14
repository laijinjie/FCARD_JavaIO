/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Door.Access.Door8800.Command.System.Parameter;

import Door.Access.Command.CommandDetail;
import Door.Access.Command.CommandParameter;

/**
 * 消防报警功能参数
 *
 * @author 赖金杰
 */
public class WriteFireAlarmOption_Parameter extends CommandParameter {

    /**
     * 消防报警功能参数.<br>
     * <ul>
     * <li>值 &emsp; 解释                                   </li>
     * <li>0 &emsp; 不启用                                 </li>
     * <li>1 &emsp; 报警输出，并开所有门，只能软件解除     </li>
     * <li>2 &emsp; 报警输出，不开所有门，只能软件解除     </li>
     * <li>3 &emsp; 有信号报警并开门，无信号解除报警并关门 </li>
     * <li>4 &emsp; 有报警信号时开一次门，就像按钮开门一样 </li>
     * </ul>
     */
    public short Option;

    public WriteFireAlarmOption_Parameter(CommandDetail detail) {
        super(detail);
    }

}

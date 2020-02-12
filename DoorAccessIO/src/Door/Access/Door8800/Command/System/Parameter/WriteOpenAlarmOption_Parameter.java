/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Door.Access.Door8800.Command.System.Parameter;

import Door.Access.Command.CommandDetail;
import Door.Access.Command.CommandParameter;

/**
 * 匪警报警参数
 *
 * @author 赖金杰
 */
public class WriteOpenAlarmOption_Parameter extends CommandParameter {

    /**
     * 匪警报警功能参数.<br/>
     * <ul>
     * <li>值 &emsp; 解释                                                                                                                                                 </li>
     * <li>0 &emsp; 关闭此功能                                                                                                                                            </li>
     * <li>1 &emsp; 所有门锁定，报警输出，蜂鸣器不响。不开门，刷卡不能解除，软件解除，解除报警后门的锁定也解锁了。                                                        </li>
     * <li>2 &emsp; 报警输出，不锁定，蜂鸣器响。不开门，刷卡可以解除，软件可以解除                                                                                        </li>
     * <li>3 &emsp;
     * 按报警按钮就报警，门锁定，并输出，不按时就停止。不开门，按钮停止时就解除，软件或刷卡不能解除。按报警按钮的时候门是处于锁定状态的，不按时解除锁定状态。</li>
     * </ul>
     */
    public short Option;

    public WriteOpenAlarmOption_Parameter(CommandDetail detail) {
        super(detail);
    }

}

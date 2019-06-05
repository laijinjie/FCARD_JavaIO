/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Net.PC15.FC8800.Command.System.Parameter;

import Net.PC15.Command.CommandDetail;
import Net.PC15.Command.CommandParameter;

/**
 * 有效期即将过期提醒时间
 *
 * @author 赖金杰
 */
public class WriteCardDeadlineTipDay_Parameter extends CommandParameter {

    /**
     * 有效期即将过期提醒时间<p/>
     * 取值范围: 1-255。0--表示关闭;默认值是30天
     */
    public short Day;

    public WriteCardDeadlineTipDay_Parameter(CommandDetail detail) {
        super(detail);
    }

}

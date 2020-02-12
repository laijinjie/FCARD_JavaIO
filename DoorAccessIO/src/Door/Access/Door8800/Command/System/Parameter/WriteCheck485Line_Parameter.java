/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Door.Access.Door8800.Command.System.Parameter;

import Door.Access.Command.CommandDetail;
import Door.Access.Command.CommandParameter;

/**
 * 485线路反接检测开关
 *
 * @author 赖金杰
 */
public class WriteCheck485Line_Parameter extends CommandParameter {

    /**
     * 485线路反接检测开关
     */
    public boolean Use;

    public WriteCheck485Line_Parameter(CommandDetail detail) {
        super(detail);
    }
}

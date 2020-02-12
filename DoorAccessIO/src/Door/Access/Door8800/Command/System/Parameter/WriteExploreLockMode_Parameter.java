/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Door.Access.Door8800.Command.System.Parameter;

import Door.Access.Command.CommandDetail;
import Door.Access.Command.CommandParameter;

/**
 * 防探测功能开关
 *
 * @author 赖金杰
 */
public class WriteExploreLockMode_Parameter extends CommandParameter {

    /**
     * 防探测功能开关
     */
    public boolean Use;

    public WriteExploreLockMode_Parameter(CommandDetail detail) {
        super(detail);
    }
}

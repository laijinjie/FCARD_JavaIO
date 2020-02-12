/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Door.Access.Door8800.Command.Door.Parameter;

import Door.Access.Command.CommandDetail;
import Door.Access.Command.CommandParameter;

/**
 * 门号参数，取值范围 1-4
 *
 * @author 赖金杰
 */
public class DoorPort_Parameter extends CommandParameter {

    /**
     * 门索引号<br/>
     * 取值范围 1-4
     */
    public int Door;

    public DoorPort_Parameter(CommandDetail detail, int iDoor) {
        super(detail);
    }

}

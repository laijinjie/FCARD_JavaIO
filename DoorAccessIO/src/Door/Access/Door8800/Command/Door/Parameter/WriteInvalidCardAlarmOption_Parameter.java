/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Door.Access.Door8800.Command.Door.Parameter;

import Door.Access.Command.CommandDetail;
import Door.Access.Command.CommandParameter;

/**
 * 未注册卡读卡时报警功能
 *
 * @author 赖金杰
 */
public class WriteInvalidCardAlarmOption_Parameter extends CommandParameter {

    /**
     * 门号<br>
     * 门端口在控制板中的索引号，取值：1-4
     */
    public int DoorNum;

    /**
     * 是否报警功能
     */
    public boolean Use;

    public WriteInvalidCardAlarmOption_Parameter(CommandDetail detail, int door, boolean use) {
        super(detail);
        this.DoorNum = door;
        this.Use = use;
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Door.Access.Door8800.Command.Door.Result;

import Door.Access.Command.INCommandResult;

/**
 * 未注册卡读卡时报警功能
 *
 * @author 赖金杰
 */
public class ReadInvalidCardAlarmOption_Result implements INCommandResult {

    /**
     * 门号<br/>
     * 门端口在控制板中的索引号，取值：1-4
     */
    public int DoorNum;

    /**
     * 是否报警功能
     */
    public boolean Use;

    public ReadInvalidCardAlarmOption_Result() {
    }

    @Override
    public void release() {
        return;
    }
}

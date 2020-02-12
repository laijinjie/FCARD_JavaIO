/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Door.Access.Door8800.Command.Door.Result;

import Door.Access.Command.INCommandResult;
import Door.Access.Door8800.Command.Data.TimeGroup.WeekTimeGroup;

/**
 * 定时锁定门，可设定一周内任意时间的门锁定时段。
 *
 * @author 赖金杰
 */
public class ReadAutoLockedSetting_Result implements INCommandResult {

    /**
     * 门号<br/>
     * 门端口在控制板中的索引号，取值：1-4
     */
    public int DoorNum;

    /**
     * 是否启用定时锁定功能
     */
    public boolean Use;
    /**
     * 定时锁定时段<br/>
     * 功能开启后，在时段内的时候门将进入自动锁定状态
     */
    public WeekTimeGroup timeGroup;

    public ReadAutoLockedSetting_Result() {
        timeGroup = new WeekTimeGroup(8);
    }

    @Override
    public void release() {
        return;
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Door.Access.Door8800.Command.Door.Result;

import Door.Access.Command.INCommandResult;
import Door.Access.Door8800.Command.Data.TimeGroup.WeekTimeGroup;

/**
 * 出门按钮功能<br/>
 * 可设定出门按钮的按下5秒后常开，还可以设定出门按钮的使用时段<br/>
 *
 * @author 赖金杰
 */
public class ReadPushButtonSetting_Result implements INCommandResult {

    /**
     * 门号<br/>
     * 门端口在控制板中的索引号，取值：1-4
     */
    public int DoorNum;

    /**
     * 是否启用出门按钮功能
     */
    public boolean Use;

    /**
     * 是否启用出门按钮常开功能<br/>
     * 出门按钮按下5秒后门进入常开状态，再次按5秒退出常开。
     */
    public boolean NormallyOpen;

    /**
     * 出门按钮的使用时段
     */
    public WeekTimeGroup TimeGroup;

    public ReadPushButtonSetting_Result() {
        TimeGroup = new WeekTimeGroup(8);
    }

    @Override
    public void release() {
        return;
    }
}

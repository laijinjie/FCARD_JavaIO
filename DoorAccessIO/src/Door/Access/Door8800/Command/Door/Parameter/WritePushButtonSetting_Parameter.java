/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Door.Access.Door8800.Command.Door.Parameter;

import Door.Access.Door8800.Command.Data.TimeGroup.WeekTimeGroup;
import Door.Access.Command.CommandDetail;
import Door.Access.Command.CommandParameter;

/**
 * 出门按钮功能<br>
 * 可设定出门按钮的按下5秒后常开，还可以设定出门按钮的使用时段<br>
 *
 * @author 赖金杰
 */
public class WritePushButtonSetting_Parameter extends CommandParameter {

    /**
     * 门号<br>
     * 门端口在控制板中的索引号，取值：1-4
     */
    public int DoorNum;

    /**
     * 是否启用出门按钮功能
     */
    public boolean Use;

    /**
     * 是否启用出门按钮常开功能<br>
     * 出门按钮按下5秒后门进入常开状态，再次按5秒退出常开。
     */
    public boolean NormallyOpen;

    /**
     * 出门按钮的使用时段
     */
    public WeekTimeGroup TimeGroup;

    public WritePushButtonSetting_Parameter(CommandDetail detail, int door, boolean use, boolean normallyOpen) {
        super(detail);
        this.DoorNum = door;
        this.Use = use;
        this.NormallyOpen = normallyOpen;
        this.TimeGroup = new WeekTimeGroup(8);
    }
}

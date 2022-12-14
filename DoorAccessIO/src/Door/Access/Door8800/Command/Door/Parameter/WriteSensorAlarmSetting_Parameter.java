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
 * 门磁报警功能<br>
 * 当无有效开门验证时（远程开门、刷卡、密码、出门按钮），检测到门磁打开时就会报警。<br>
 *
 * @author 赖金杰
 */
public class WriteSensorAlarmSetting_Parameter extends CommandParameter {

    /**
     * 门号<br>
     * 门端口在控制板中的索引号，取值：1-4
     */
    public int DoorNum;

    /**
     * 是否启用门磁报警功能
     */
    public boolean Use;

    /**
     * 门磁报警不报警时段<br>
     * 注意：这里的时段规定的是不报警时段，即在功能开启后，如果在时段内，门磁随意打开不会报警。
     */
    public WeekTimeGroup TimeGroup;

    public WriteSensorAlarmSetting_Parameter(CommandDetail detail, int door, boolean use) {
        super(detail);
        this.DoorNum = door;
        this.Use = use;
        this.TimeGroup = new WeekTimeGroup(8);
    }

}

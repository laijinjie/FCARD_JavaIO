/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Door.Access.Door8800.Command.Door.Parameter;

import Door.Access.Command.CommandDetail;
import Door.Access.Command.CommandParameter;

/**
 * 开门超时报警功能<br/>
 * 门磁打开超过一定时间后就会报警和发出提示语音和响声。<br/>
 *
 * @author 赖金杰
 */
public class WriteOvertimeAlarmSetting_Parameter extends CommandParameter {

    /**
     * 门号<br/>
     * 门端口在控制板中的索引号，取值：1-4
     */
    public int DoorNum;

    /**
     * 是否启用胁迫报警功能
     */
    public boolean Use;

    /**
     * 超时时间，指门磁一直打开的时间。<br/>
     * 取值范围：0-65535,0表示关闭；单位秒；
     */
    public int Overtime;

    /**
     * 在开门超时后，是否报警输出
     */
    public boolean Alarm;

    public WriteOvertimeAlarmSetting_Parameter(CommandDetail detail, int door, boolean use, int overtime, boolean alarm) {
        super(detail);
        this.DoorNum = door;
        this.Use = use;
        this.Overtime = overtime;
        this.Alarm = alarm;
    }

}

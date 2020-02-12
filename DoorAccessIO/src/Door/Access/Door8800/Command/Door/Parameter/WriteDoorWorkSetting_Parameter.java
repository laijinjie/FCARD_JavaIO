/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Door.Access.Door8800.Command.Door.Parameter;

import Door.Access.Command.CommandDetail;
import Door.Access.Command.CommandParameter;
import Door.Access.Door8800.Command.Data.TimeGroup.WeekTimeGroup;

/**
 * 门的工作模式，可设定多卡、首卡、常开 这三种特殊工作模式。
 *
 * @author 赖金杰
 */
public class WriteDoorWorkSetting_Parameter extends CommandParameter {

    /**
     * 门号<br/>
     * 门端口在控制板中的索引号，取值：1-4
     */
    public int DoorNum;

    /**
     * 是否启用门工作参数
     */
    public boolean Use;

    /**
     * 门工作方式；<br/>
     * <ul>
     * <li>1 &emsp; 普通        </li>
     * <li>2 &emsp; 多卡        </li>
     * <li>3 &emsp; 首卡（时段）</li>
     * <li>4 &emsp; 常开（时段）</li>
     * </ul>
     *
     */
    public int DoorWorkType;

    /**
     * 常开工作模式选项<br/>
     * <ul>
     * <li>1 &emsp; 合法卡在时段内即可常开                      </li>
     * <li>2 &emsp; 授权中标记为常开卡的在指定时段内刷卡即可常开</li>
     * <li>3 &emsp; 自动开关，到时间自动开关门。                </li>
     * </ul>
     */
    public int HoldDoorOption;

    /**
     * 周时段<br/>
     * 用于在工作模式设定为首卡，常开时，在时段内会生效。 首卡模式在时段外工作方式需要参考 首卡开门参数。 常开模式时段外会自动退出常开。
     */
    public WeekTimeGroup timeGroup;

    public WriteDoorWorkSetting_Parameter(CommandDetail detail) {
        super(detail);
        timeGroup = new WeekTimeGroup(8);
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Door.Access.Door8800.Command.Door.Parameter;

import Door.Access.Command.CommandDetail;
import Door.Access.Command.CommandParameter;

/**
 * 开门保持时间<br>
 * 继电器开锁后释放时间<br>
 *
 * @author 赖金杰
 */
public class WriteRelayReleaseTime_Parameter extends CommandParameter {

    /**
     * 门号<br>
     */
    public int Door;

    /**
     * 开门保持时间<br>
     * 继电器开锁后释放时间<br>
     * 取值范围：0-65535；0表示0.5秒。单位：秒；
     */
    public int ReleaseTime;

    public WriteRelayReleaseTime_Parameter(CommandDetail detail, int door, int time) {
        super(detail);
        this.Door = door;
        this.ReleaseTime = time;
    }

}

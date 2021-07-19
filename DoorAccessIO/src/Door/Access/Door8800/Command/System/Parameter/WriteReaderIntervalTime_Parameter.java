/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Door.Access.Door8800.Command.System.Parameter;

import Door.Access.Command.CommandDetail;
import Door.Access.Command.CommandParameter;

/**
 * 读卡间隔时间参数
 * @author 赖金杰
 */
public class WriteReaderIntervalTime_Parameter extends CommandParameter {
    /**
     * 读卡间隔时间. <br>
     * 最大65535秒。0表示无限制
     */
    public int IntervalTime;
    public WriteReaderIntervalTime_Parameter(CommandDetail detail) {
        super(detail);
    }
    
}

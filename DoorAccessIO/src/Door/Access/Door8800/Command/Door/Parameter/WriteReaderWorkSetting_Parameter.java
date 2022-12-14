/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Door.Access.Door8800.Command.Door.Parameter;

import Door.Access.Command.CommandDetail;
import Door.Access.Command.CommandParameter;
import Door.Access.Door8800.Command.Data.TimeGroup.WeekTimeGroup_ReaderWork;

/**
 * 设置读卡验证方式的参数
 *
 * @author 赖金杰
 */
public class WriteReaderWorkSetting_Parameter extends CommandParameter {

    /**
     * 读卡验证方式的周时段
     */
    public WeekTimeGroup_ReaderWork ReaderWork;
    /**
     * 要设置验证方式的门号
     */
    public int DoorNum;

    public WriteReaderWorkSetting_Parameter(CommandDetail detail, int door) {
        super(detail);
        ReaderWork = new WeekTimeGroup_ReaderWork(8);
        DoorNum = door;
    }

}

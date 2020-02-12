/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Door.Access.Door8800.Command.Door.Result;

import Door.Access.Command.INCommandResult;
import Door.Access.Door8800.Command.Data.TimeGroup.WeekTimeGroup_ReaderWork;

/**
 * 读取读卡验证方式的返回值
 *
 * @author 赖金杰
 */
public class ReadReaderWorkSetting_Result implements INCommandResult {

    /**
     * 读卡验证方式的周时段
     */
    public WeekTimeGroup_ReaderWork ReaderWork;
    /**
     * 门号
     */
    public int DoorNum;

    public ReadReaderWorkSetting_Result() {
        ReaderWork = new WeekTimeGroup_ReaderWork(8);
    }

    @Override
    public void release() {
        ReaderWork = null;
        return;
    }

}

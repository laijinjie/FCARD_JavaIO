/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Door.Access.Door8800.Command.System.Result;

import Door.Access.Command.INCommandResult;

/**
 * 读取消防报警状态
 *
 * @author 赖金杰
 */
public class ReadFireAlarmState_Result implements INCommandResult {

    /**
     * 消防报警.<br>
     * 状态：0--未开启报警；1--已开启报警
     */
    public short FireAlarm;

    @Override
    public void release() {
        return;
    }

}

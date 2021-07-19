/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Door.Access.Door8800.Command.System.Result;

import Door.Access.Command.INCommandResult;

/**
 *
 * @author 赖金杰
 */
public class ReadSmogAlarmState_Result implements INCommandResult {

    /**
     * 烟雾报警参数.<br>
     * 状态：0--未开启报警；1--已开启报警
     */
    public short SmogAlarm;

    @Override
    public void release() {
        return;
    }
    
}

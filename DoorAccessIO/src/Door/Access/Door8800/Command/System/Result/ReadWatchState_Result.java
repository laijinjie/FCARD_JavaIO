/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Door.Access.Door8800.Command.System.Result;

import Door.Access.Command.INCommandResult;
import Door.Access.Door8800.Command.Data.BroadcastDetail;

/**
 * 读取实时监控状态
 *
 * @author 赖金杰
 */
public class ReadWatchState_Result implements INCommandResult {

    /**
     * 监控状态.<br>
     * 0--未开启监控；1--已开启监控
     */
    public int State;

    @Override
    public void release() {
        return;
    }

}

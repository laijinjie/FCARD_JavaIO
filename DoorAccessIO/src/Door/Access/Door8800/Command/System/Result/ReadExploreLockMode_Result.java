/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Door.Access.Door8800.Command.System.Result;

import Door.Access.Command.INCommandResult;

/**
 * 防探测功能开关
 *
 * @author 赖金杰
 */
public class ReadExploreLockMode_Result implements INCommandResult {

    /**
     * 防探测功能开关
     */
    public boolean Use;

    @Override
    public void release() {
        return;
    }
}

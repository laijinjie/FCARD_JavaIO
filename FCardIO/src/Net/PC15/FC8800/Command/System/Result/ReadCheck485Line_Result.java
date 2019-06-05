/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Net.PC15.FC8800.Command.System.Result;

import Net.PC15.Command.INCommandResult;

/**
 * 485线路反接检测开关
 *
 * @author 赖金杰
 */
public class ReadCheck485Line_Result implements INCommandResult {

    /**
     * 485线路反接检测开关
     */
    public boolean Use;

    @Override
    public void release() {
        return;
    }

}

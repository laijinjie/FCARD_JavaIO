/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Net.PC15.FC8800.Command.System.Result;

import Net.PC15.Command.INCommandResult;
import Net.PC15.FC8800.Command.Data.TheftAlarmSetting;

/**
 * 智能防盗主机参数
 *
 * @author 赖金杰
 */
public class ReadTheftAlarmSetting_Result implements INCommandResult {

    /**
     * 智能防盗主机参数
     */
    public TheftAlarmSetting Setting;

    @Override
    public void release() {
        return;
    }

}

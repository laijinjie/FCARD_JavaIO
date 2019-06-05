/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Net.PC15.FC8800.Command.Door.Result;

import Net.PC15.Command.INCommandResult;

/**
 * 开门保持时间<br/>
 * 继电器开锁后释放时间<br/>
 *
 * @author 赖金杰
 */
public class ReadRelayReleaseTime_Result implements INCommandResult {

    /**
     * 门号<br/>
     */
    public int Door;

    /**
     * 开门保持时间<br/>
     * 继电器开锁后释放时间<br/>
     * 取值范围：0-65535；0表示0.5秒。单位：秒；
     */
    public int ReleaseTime;

    public ReadRelayReleaseTime_Result() {

    }

    @Override
    public void release() {
    }

}

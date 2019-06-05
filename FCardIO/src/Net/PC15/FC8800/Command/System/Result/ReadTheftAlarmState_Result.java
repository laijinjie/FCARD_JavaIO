/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Net.PC15.FC8800.Command.System.Result;

import Net.PC15.Command.INCommandResult;

/**
 * 获取防盗主机布防状态
 *
 * @author 赖金杰
 */
public class ReadTheftAlarmState_Result implements INCommandResult {

    /**
     * 布防状态.<br/>
     * <ul>
     * <li>1 &emsp; 延时布防              </li>
     * <li>2 &emsp; 已布防                </li>
     * <li>3 &emsp; 延时撤防              </li>
     * <li>4 &emsp; 未布防                </li>
     * <li>5 &emsp; 报警延时，准备启用报警</li>
     * <li>6 &emsp; 防盗报警已启动        </li>
     * </ul>
     */
    public int TheftState;

    /**
     * 防盗主机报警状态.<br/>
     * 0未报警，1已报警
     */
    public int TheftAlarm;

    @Override
    public void release() {
        return;
    }

}

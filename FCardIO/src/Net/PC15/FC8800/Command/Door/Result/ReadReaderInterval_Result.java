/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Net.PC15.FC8800.Command.Door.Result;

import Net.PC15.Command.INCommandResult;

/**
 * 重复读卡间隔
 *
 * @author 赖金杰
 */
public class ReadReaderInterval_Result implements INCommandResult {

    /**
     * 门号<br/>
     * 门端口在控制板中的索引号，取值：1-4
     */
    public int DoorNum;

    /**
     * 是否重复读卡间隔功能
     */
    public boolean Use;

    /**
     * 重复读卡时的记录响应模式<br/>
     * *<ul>
     * <li>1 &emsp; 记录读卡，不开门，有提示   </li>
     * <li>2 &emsp; 不记录读卡，不开门，有提示 </li>
     * <li>3 &emsp; 不做响应，无提示           </li>
     * </ul>
     */
    public int RecordOption;

    public ReadReaderInterval_Result() {
    }

    @Override
    public void release() {
        return;
    }
}

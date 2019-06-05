/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Net.PC15.FC8800.Command.System.Result;

import Net.PC15.Command.INCommandResult;
import Net.PC15.FC8800.Command.Data.BroadcastDetail;

/**
 * 语音段开关参数
 *
 * @author 赖金杰
 */
public class ReadBroadcast_Result implements INCommandResult {

    /**
     * 语音段开关.<br/>
     * 语音段对照可参考《FC8800语音表》 每个开关true 表示启用，false 表示禁用
     */
    public BroadcastDetail Broadcast;

    @Override
    public void release() {
        return;
    }

}

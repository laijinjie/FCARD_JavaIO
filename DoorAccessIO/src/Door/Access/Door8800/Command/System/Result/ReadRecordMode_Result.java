/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Door.Access.Door8800.Command.System.Result;

import Door.Access.Command.INCommandResult;

/**
 * 记录存储方式
 *
 * @author 赖金杰
 */
public class ReadRecordMode_Result implements INCommandResult {

    /**
     * 记录存储方式.<br>
     * 0是满循环，1表示满不循环
     */
    public short RecordMode;

    @Override
    public void release() {
        return;
    }
}

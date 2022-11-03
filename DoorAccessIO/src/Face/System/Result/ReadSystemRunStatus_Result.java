/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Face.System.Result;

import Door.Access.Command.INCommandResult;

/**
 * 读取系统运行状态
 *
 * @author F
 */
public class ReadSystemRunStatus_Result implements INCommandResult {

    /**
     * 系统运行天数
     */
    public int runDay;
    /**
     * 格式化次数
     */
    public int formatCount;
    /**
     * 看门狗复位次数
     */
    public int restartCount;
    /**
     * 上电时间
     */
    public String startTime;

    @Override
    public void release() {
        return;
    }

}

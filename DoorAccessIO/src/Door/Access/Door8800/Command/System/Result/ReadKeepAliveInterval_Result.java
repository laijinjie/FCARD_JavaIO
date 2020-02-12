/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Door.Access.Door8800.Command.System.Result;

import Door.Access.Command.INCommandResult;

/**
 * 读取控制器作为客户端时，和服务器的保活间隔时间
 *
 * @author 赖金杰
 */
public class ReadKeepAliveInterval_Result implements INCommandResult {

    /**
     * 保活间隔时间.<br>
     * 取值范围：0-3600,0--关闭功能,
     */
    public int IntervalTime;

    @Override
    public void release() {
        return;
    }

}

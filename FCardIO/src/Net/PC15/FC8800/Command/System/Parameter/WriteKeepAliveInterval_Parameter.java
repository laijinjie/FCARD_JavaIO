/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Net.PC15.FC8800.Command.System.Parameter;

import Net.PC15.Command.CommandDetail;
import Net.PC15.Command.CommandParameter;

/**
 * 设置控制器作为客户端时，和服务器的保活间隔时间
 *
 * @author 赖金杰
 */
public class WriteKeepAliveInterval_Parameter extends CommandParameter {

    /**
     * 保活间隔时间.<br>
     * 取值范围：0-3600,0--关闭功能,
     */
    public int IntervalTime;

    public WriteKeepAliveInterval_Parameter(CommandDetail detail) {
        super(detail);
    }

}

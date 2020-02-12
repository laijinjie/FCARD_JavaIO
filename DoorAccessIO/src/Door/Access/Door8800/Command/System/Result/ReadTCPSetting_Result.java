/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Door.Access.Door8800.Command.System.Result;

import Door.Access.Command.INCommandResult;
import Door.Access.Door8800.Command.Data.TCPDetail;

/**
 * 读取TCP详情的返回值
 *
 * @author 赖金杰
 */
public class ReadTCPSetting_Result implements INCommandResult {

    /**
     * 控制器的TCP 网络参数
     */
    public final TCPDetail TCP;

    public ReadTCPSetting_Result(TCPDetail tcp) {
        this.TCP = tcp;
    }

    @Override
    public void release() {
        return;
    }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Door.Access.Door8800.Command.System.Parameter;

import Door.Access.Command.CommandDetail;
import Door.Access.Command.CommandParameter;
import Door.Access.Door8800.Command.Data.TCPDetail;

/**
 * 修改控制器TCP网络参数
 *
 * @author 赖金杰
 */
public class WriteTCPSetting_Parameter extends CommandParameter {

    /**
     * 控制器TCP网络参数
     */
    private TCPDetail _TCP;

    public WriteTCPSetting_Parameter(CommandDetail detail, TCPDetail tcp) {
        super(detail);
        SetTCP(tcp);
    }

    /**
     * 获取控制器TCP网络参数
     * @return  TCP网络参数
     */
    public TCPDetail GetTCP() {
        return _TCP;
    }

    /**
     * 设置控制器TCP网络参数
     * @param tcp TCP网络参数
     */
    public void SetTCP(TCPDetail tcp) {
        _TCP = tcp;
    }

}

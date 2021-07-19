/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Door.Access.Door8800.Command.System;

import Door.Access.Command.CommandParameter;
import Door.Access.Door8800.Command.Door8800Command;

/**
 * 初始化控制器.<br>
 * 控制器初始化后的数据状态：清空所有授权卡，清空所有节假日，清空所有开门时段，清空所有密码，清空所有记录，复位键盘密码，开锁保持时间为3秒
 *
 * @author 赖金杰
 */
public class FormatController extends Door8800Command {

    public FormatController(CommandParameter par) {
        _Parameter = par;
        CreatePacket(1, 0xF, 0);
    }

    @Override
    protected void Release0() {
        return;
    }

}

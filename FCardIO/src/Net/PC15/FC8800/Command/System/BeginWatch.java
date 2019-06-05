/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Net.PC15.FC8800.Command.System;

import Net.PC15.Command.CommandParameter;
import Net.PC15.FC8800.Command.FC8800Command;

/**
 * 开始数据监控.<br/>
 * 数据监控打开后，刷卡时会触发事件 {@link Net.PC15.Connector.INConnectorEvent#WatchEvent}
 *
 * @author 赖金杰
 */
public class BeginWatch extends FC8800Command {

    public BeginWatch(CommandParameter par) {
        _Parameter = par;
        CreatePacket(1, 0xB, 0);
    }

    @Override
    protected void Release0() {
        return;
    }

}

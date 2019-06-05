/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Net.PC15.FC8800.Command.System;

import Net.PC15.Command.CommandParameter;
import Net.PC15.Connector.INConnectorEvent;
import Net.PC15.FC8800.Command.FC8800Command;
import Net.PC15.FC8800.Command.System.Result.ReadKeyboard_Result;
import Net.PC15.FC8800.Packet.FC8800PacketModel;
import io.netty.buffer.ByteBuf;

/**
 * 读卡器密码键盘启用功能开关.<br/>
 * 成功返回结果参考 {@link ReadKeyboard_Result}
 *
 * @author 赖金杰
 */
public class ReadKeyboard extends FC8800Command {

    public ReadKeyboard(CommandParameter par) {
        _Parameter = par;
        CreatePacket(1, 0xA, 0x82);
    }

    @Override
    protected void Release0() {
        return;
    }

    @Override
    protected boolean _CommandStep(INConnectorEvent oEvent, FC8800PacketModel model) {
        if (CheckResponse_Cmd(model, 1, 0xA, 0x82, 1)) {
            ByteBuf buf = model.GetDatabuff();
            //设定返回值
            ReadKeyboard_Result ret = new ReadKeyboard_Result();
            _Result = ret;
            ret.Keyboard = buf.readUnsignedByte();

            RaiseCommandCompleteEvent(oEvent);
            return true;
        } else {
            return false;
        }
    }

}

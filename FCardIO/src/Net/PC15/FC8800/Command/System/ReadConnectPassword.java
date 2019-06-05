/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Net.PC15.FC8800.Command.System;

import Net.PC15.Command.CommandParameter;
import Net.PC15.Connector.INConnectorEvent;
import Net.PC15.FC8800.Command.FC8800Command;
import Net.PC15.FC8800.Command.System.Result.ReadConnectPassword_Result;
import Net.PC15.FC8800.Packet.FC8800PacketModel;
import Net.PC15.Util.ByteUtil;
import io.netty.buffer.ByteBuf;

/**
 * 从控制器中获取通讯密码
 * <p>
 * 成功返回结果参考 {@link ReadConnectPassword_Result}
 *
 * @author 赖金杰
 */
public class ReadConnectPassword extends FC8800Command {

    private static final byte[] DataStrt;

    static {
        //46 43 61 72 64 59 7A
        DataStrt = new byte[]{(byte) 0x46, (byte) 0x43, (byte) 0x61, (byte) 0x72, (byte) 0x64, (byte) 0x59, (byte) 0x7A};
    }

    public ReadConnectPassword(CommandParameter par) {
        _Parameter = par;
        ByteBuf dataBuf = ByteUtil.ALLOCATOR.buffer(7);
        dataBuf.writeBytes(DataStrt);
        CreatePacket(1, 4, 0, 7, dataBuf);
    }

    @Override
    protected void Release0() {
        return;
    }

    @Override
    protected boolean _CommandStep(INConnectorEvent oEvent, FC8800PacketModel model) {
        if (CheckResponse_Cmd(model, 1, 4, 0, 4)) {
            ByteBuf buf = model.GetDatabuff();
            byte[] PwdBuf = new byte[4];
            String pwd;
            try {
                buf.readBytes(PwdBuf);

                pwd = ByteUtil.ByteToHex(PwdBuf);
                //System.out.println("收到SN：" +  SN);
            } catch (Exception e) {
                pwd = null;
            }
            //设定返回值
            _Result = new ReadConnectPassword_Result(pwd);

            RaiseCommandCompleteEvent(oEvent);
            return true;
        } else {
            return false;
        }
    }

}

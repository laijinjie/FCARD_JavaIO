/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Net.PC15.FC8800.Command.System;

import Net.PC15.Command.CommandParameter;
import Net.PC15.Connector.INConnectorEvent;
import Net.PC15.FC8800.Command.FC8800Command;
import Net.PC15.FC8800.Command.System.Result.ReadSN_Result;
import Net.PC15.FC8800.Packet.FC8800PacketModel;
import io.netty.buffer.ByteBuf;

/**
 * 从控制器中读取SN，此指令是广播读取，无需提前知道控制器SN.
 * <p>
 * 调用此指令SN可以使用：0000000000000000 命令
 * <p>
 * 成功返回结果参考 {@link ReadSN_Result}
 *
 * @author 赖金杰
 */
public class ReadSN extends FC8800Command {

    public ReadSN(CommandParameter par) {
        _Parameter = par;

        CreatePacket(1, 2);
    }

    @Override
    protected void Release0() {
        return;
    }

    @Override
    protected boolean _CommandStep(INConnectorEvent oEvent, FC8800PacketModel model) {
        if (CheckResponse_Cmd(model, 1, 2, 0, 16)) {
            ByteBuf buf = model.GetDatabuff();
            byte[] SNBuf = new byte[16];
            String SN;
            try {
                buf.readBytes(SNBuf);

                SN = new String(SNBuf, "GB2312");
                //System.out.println("收到SN：" +  SN);
            } catch (Exception e) {
                SN = null;
            }
            //设定返回值
            _Result = new ReadSN_Result(SN);

            RaiseCommandCompleteEvent(oEvent);
            return true;
        } else {
            return false;
        }
    }

}

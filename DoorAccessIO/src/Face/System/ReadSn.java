package Face.System;

import Door.Access.Command.CommandParameter;
import Door.Access.Connector.INConnectorEvent;
import Door.Access.Door8800.Command.Door8800Command;
import Door.Access.Door8800.Packet.Door8800PacketModel;
import Face.System.Result.ReadSn_Result;
import io.netty.buffer.ByteBuf;

/**
 * 读取设备SN
 */
public class ReadSn extends Door8800Command {
    /**
     * 初始化命令
     * @param parameter 命令连接信息
     */
    public ReadSn(CommandParameter parameter) {
        _Parameter = parameter;
        CreatePacket(0x01, 0x02, 0x00);
    }

    @Override
    protected boolean _CommandStep(INConnectorEvent oEvent, Door8800PacketModel model) {
        if(CheckResponse_Cmd(model,0x01,0x02,0x00,0x10)){
            ByteBuf buf = model.GetDatabuff();
            byte[] SNBuf = new byte[0x10];
            String SN;
            try {
                buf.readBytes(SNBuf);

                SN = new String(SNBuf, "GB2312");
                //System.out.println("收到SN：" +  SN);
            } catch (Exception e) {
                SN = null;
            }
            _Result =new ReadSn_Result(SN);
        }
        RaiseCommandCompleteEvent(oEvent);
        return true;
    }

    @Override
    protected void Release0() {

    }
}

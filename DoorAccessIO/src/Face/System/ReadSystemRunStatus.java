package Face.System;

import Door.Access.Command.CommandParameter;
import Door.Access.Connector.INConnectorEvent;
import Door.Access.Door8800.Command.Door8800Command;
import Door.Access.Door8800.Packet.Door8800PacketModel;
import Face.System.Result.ReadSystemRunStatus_Result;
import io.netty.buffer.ByteBuf;

/**
 * 读取系统运行状态
 */
public class ReadSystemRunStatus extends Door8800Command {
/**
 * 读取运行状态
 * @param par 命令参数
 */
    public ReadSystemRunStatus(CommandParameter par) {
        _Parameter = par;
        CreatePacket(1, 9);
    }

    @Override
    protected void Release0() {
        return;
    }

    @Override
    protected boolean _CommandStep(INConnectorEvent oEvent, Door8800PacketModel model) {
        if (CheckResponse_Cmd(model, 0x1, 9, 0)) {
            ByteBuf buf = model.GetDatabuff();
            ReadSystemRunStatus_Result result = new ReadSystemRunStatus_Result();
            _Result=result;
            result.runDay = buf.readUnsignedShort();
            result.formatCount = buf.readUnsignedShort();
            result.restartCount = buf.readUnsignedShort();

            //秒
            String second = Integer.toHexString(buf.readByte());
            //分
            String branch = Integer.toHexString(buf.readByte());
            //时
            String hour = Integer.toHexString(buf.readByte());
            //日
            String day = Integer.toHexString(buf.readByte());
            //月
            String month = Integer.toHexString(buf.readByte());

            //年
            String year = "20" + Integer.toHexString(buf.readByte());
            result.startTime = String.format("%s-%s-%s %s:%s:%s", year, month, day, hour, branch, second);
            RaiseCommandCompleteEvent(oEvent);

            return true;
        } else {
            return false;
        }
    }
}

package Face.TimeGroup;

import Door.Access.Command.CommandParameter;
import Door.Access.Connector.INConnectorEvent;
import Door.Access.Door8800.Command.Data.TimeGroup.WeekTimeGroup;
import Door.Access.Door8800.Command.Door8800Command;
import Door.Access.Door8800.Packet.Door8800PacketModel;
import Face.TimeGroup.Result.ReadTimeGroup_Result;
import io.netty.buffer.ByteBuf;

/**
 * 读取开门时段
 */
public class ReadTimeGroup extends Door8800Command {
    /**
     * 初始化对象
     * @param parameter 命令参数
     */
    public ReadTimeGroup(CommandParameter parameter) {
        _Parameter = parameter;
        _ProcessMax = 64;
        ReadTimeGroup_Result result = new ReadTimeGroup_Result();
        result.ListWeekTimeGroup.clear();
        _Result = result;
        CreatePacket(0x06, 0x02);
    }

    @Override
    protected boolean _CommandStep(INConnectorEvent oEvent, Door8800PacketModel model) {

        if (CheckResponse_Cmd(model,0x06, 0x02, 0xff, 4)) {
            RaiseCommandCompleteEvent(oEvent);
            return true;
        }

        if (CheckResponse_Cmd(model,0x06,0x02)) {
            ByteBuf buf = model.GetDatabuff();
            SetBytes(buf);
            _ProcessStep++;
            RaiseCommandProcessEvent(oEvent);
            CommandWaitResponse();
            return true;
        }

        return super._CommandStep(oEvent, model);
    }

    /**
     * 解析开门时段
     * @param buf 数据缓冲区
     */
    private void SetBytes(ByteBuf buf) {
        ReadTimeGroup_Result result = (ReadTimeGroup_Result) _Result;

        //64个IByteBuffer，每个包含组 号2byte+224byte(7*8*4(时分-时分))
        WeekTimeGroup wtg = new WeekTimeGroup(8);
        buf.readShort();
        wtg.SetBytes(buf);
        result.ListWeekTimeGroup.add(wtg);
        result.Count++;
    }

    @Override
    protected void Release0() {

    }
}

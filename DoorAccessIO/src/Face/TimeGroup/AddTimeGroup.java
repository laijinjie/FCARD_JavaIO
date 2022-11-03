package Face.TimeGroup;

import Door.Access.Connector.INConnectorEvent;
import Door.Access.Door8800.Command.Door8800Command;
import Door.Access.Door8800.Packet.Door8800PacketModel;
import Door.Access.Util.ByteUtil;
import Face.TimeGroup.Parameter.AddTimeGroup_Parameter;
import io.netty.buffer.ByteBuf;

public class AddTimeGroup extends Door8800Command {
    /**
     * 开门时段参数
     */
    private AddTimeGroup_Parameter parameter;
    /**
     * 写入索引
     */
    protected int writeIndex = 0;

    /**
     * 初始化对象
     *
     * @param parameter 开门时段参数
     */
    public AddTimeGroup(AddTimeGroup_Parameter parameter) {
        _Parameter = parameter;
        this.parameter = parameter;
        _ProcessMax = parameter.ListWeekTimeGroup.size();
        int dataLen = parameter.DataLen();
        CreatePacket(0x06, 0x03, 0x00, dataLen, GetBytes(dataLen));
    }

    /**
     * 获取缓冲区
     *
     * @param dataLen
     * @return
     */
    private ByteBuf GetBytes(int dataLen) {
        ByteBuf buf = ByteUtil.ALLOCATOR.buffer(dataLen);
        buf.writeByte(writeIndex + 1);
        parameter.ListWeekTimeGroup.get(writeIndex).GetBytes(buf);
        writeIndex++;
        return buf;
    }

    @Override
    protected boolean _CommandStep(INConnectorEvent oEvent, Door8800PacketModel model) {
        if (CheckResponseOK(model)) {
            _ProcessStep++;
            RaiseCommandProcessEvent(oEvent);
            if (writeIndex < _ProcessMax) {
                int dataLen = parameter.DataLen();
                CreatePacket(0x06, 0x03, 0x00, dataLen, GetBytes(dataLen));
               // CommandWaitResponse();
            } else {
                RaiseCommandCompleteEvent(oEvent);
            }
            return true;
        }
        return false;
    }

    @Override
    protected void Release0() {

    }
}

package Face.Door;

import Door.Access.Door8800.Command.Door8800Command;
import Door.Access.Util.ByteUtil;
import Face.Door.Parameter.VoiceBroadcastSetting_Parameter;
import io.netty.buffer.ByteBuf;

/**
 *设置语音播报_写入
 * @author F
 */
public class WriteVoiceBroadcastSetting extends Door8800Command {
    int DATA_LEN = 1;
/**
 * 设置语音播报_写入
 * @param parameter 语音播报写入参数
 */
    public WriteVoiceBroadcastSetting(VoiceBroadcastSetting_Parameter parameter) {
        _Parameter = parameter;
        CreatePacket(0x03, 0x13, 0x00, DATA_LEN, getDataBuf(parameter));
    }

    private ByteBuf getDataBuf(VoiceBroadcastSetting_Parameter parameter) {
        ByteBuf buf = ByteUtil.ALLOCATOR.buffer(DATA_LEN);
        buf.writeBoolean(parameter.Use);
        return buf;
    }

    @Override
    protected void Release0() {

    }
}

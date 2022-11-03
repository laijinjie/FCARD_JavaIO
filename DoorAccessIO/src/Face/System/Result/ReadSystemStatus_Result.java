package Face.System.Result;

import Door.Access.Command.INCommandResult;
import io.netty.buffer.ByteBuf;

/**
 * 读取设备状态返回结果
 */
public class ReadSystemStatus_Result implements INCommandResult {

    /**
     * 继电器物状态<br>
     * 0 - COM和NC常闭<br>
     * 1 - COM和NO常闭
     */
    public byte Relay;
    /**
     * 运行状态<br>
     * 0 - 常闭<br>
     * 1 - 常开
     */
    public byte RunState;
    /**
     * 门磁开关<br>
     * 0 - 关<br>
     * 1 - 开
     */
    public byte GateMagnetic;
    /**
     * 门报警状态 0-正常 1-报警 <br>
     * 0 - 非法认证报警<br>
     * 1 - 门磁报警<br>
     * 2 - 胁迫报警<br>
     * 3 - 开门超时报警<br>
     * 4 - 黑名单报警<br>
     * 5 - 防拆报警<br>
     * 6 - 消防报警<br>
     */
    public int[] AlarmState;
    /**
     * 锁定状态<br>
     * 0 - 未锁定<br>
     * 1 - 锁定
     */
    public byte LockState;
    /**
     * 监控状态<br>
     * 0 - 未开启监控<br>
     * 1 - 开启监控
     */
    public byte WatchState;
    /**
     * 返回值内容(响应数据包)
     */
    public byte[] ResultContent;

    /**
     * 内部使用
     * @param buf 命令响应数据包解析
     */
    public void SetBytes(ByteBuf buf) {
        int iReadIndex = buf.readerIndex();
        ResultContent = new byte[buf.readableBytes()];
        buf.readBytes(ResultContent);
        buf.readerIndex(iReadIndex);
        Relay = buf.readByte();
        RunState = buf.readByte();
        GateMagnetic = buf.readByte();
        byte b = buf.readByte();
        LockState = buf.readByte();
        WatchState = buf.readByte();
        AlarmState = new int[7];
        AlarmState[0] = ((b >> 0) & 0x1);
        AlarmState[1] = ((b >> 1) & 0x1);
        AlarmState[2] = ((b >> 2) & 0x1);
        AlarmState[3] = ((b >> 3) & 0x1);
        AlarmState[4] = ((b >> 4) & 0x1);
        AlarmState[5] = ((b >> 5) & 0x1);
        AlarmState[6] = ((b >> 6) & 0x1);
    }

    @Override
    public void release() {

    }
}

package Face.Door.Parameter;

import Door.Access.Command.CommandDetail;
import Door.Access.Command.CommandParameter;

/**
 * 设置语音播报_参数
 *
 * @author F
 */
public class VoiceBroadcastSetting_Parameter extends CommandParameter {

    /**
     * 是否启用语音播报
     */
    public boolean Use;

    /**
     * 设置语音播报_参数
     *
     * @param detail 命令详情
     * @param use 是否启用
     */
    public VoiceBroadcastSetting_Parameter(CommandDetail detail, boolean use) {
        super(detail);
        Use = use;
    }
}

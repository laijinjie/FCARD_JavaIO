package Face.Door.Result;

import Door.Access.Command.INCommandResult;

/**
 *语音播报功能_返回结果
 * @author F
 */
public class VoiceBroadcastSetting_Result implements INCommandResult {
    /**
     * 是否启用语音播报功能
     */
    public boolean Use;
    @Override
    public void release() {

    }
}

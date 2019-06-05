/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Net.PC15.FC8800.Command.System.Result;
import Net.PC15.Command.INCommandResult;
import Net.PC15.FC8800.Command.Data.ReadCardSpeak;
/**
 * 定时读卡播报语音参数
 * @author 赖金杰
 */
public class ReadReadCardSpeak_Result implements INCommandResult{
    /**
     * 定时读卡播报语音参数
     */
    public ReadCardSpeak SpeakSetting;
    
    @Override
    public void release() {
        return;
    }
    
}

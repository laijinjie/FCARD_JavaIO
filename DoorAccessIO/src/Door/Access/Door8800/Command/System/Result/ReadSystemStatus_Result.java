/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Door.Access.Door8800.Command.System.Result;

import Door.Access.Command.INCommandResult;
import java.util.Calendar;

/**
 * 设备运行信息
 * @author 赖金杰
 */
public class ReadSystemStatus_Result implements INCommandResult{
    /**
     * 系统运行天数.
     */
    public int RunDay;
    /**
     * 格式化次数.
     */
    public int FormatCount;
    /**
     * 看门狗复位次数.
     */
    public int RestartCount;
    /**
     * UPS供电状态.<br>
     * 0--表示电源取电；1--表示UPS供电
     */
    public int UPS;
    /**
     * 系统温度.
     */
    public float Temperature;
    /**
     * 上电时间.
     */
    public Calendar StartTime;
    /**
     * 电压.
     */
    public float Voltage;
    @Override
    public void release() {
        return;
    }
    
}

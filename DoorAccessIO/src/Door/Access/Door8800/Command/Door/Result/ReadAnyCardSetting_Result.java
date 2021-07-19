/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Door.Access.Door8800.Command.Door.Result;
import Door.Access.Command.INCommandResult;
/**
 * 全卡开门功能<br>
 * 所有的卡都能开门，不需要权限首选注册，只要读卡器能识别就能开门。<br>
 * 
 * @author 赖金杰
 */
public class ReadAnyCardSetting_Result implements INCommandResult {

    /**
     * 门号<br>
     * 门端口在控制板中的索引号，取值：1-4
     */
    public int DoorNum;

    /**
     * 是否启用全卡开门功能
     */
    public boolean Use;

    /**
     * 是否启用在刷卡开门后保存卡片权限<br>
     * 保存后，以后关闭全卡功能，此卡也能开门。
     */
    public boolean AutoSave;
    
    /**
     * 当 AutoSave = true; 时，可设定在哪个时段刷卡才会进行注册。<br>
     * 取值范围：1-64
     */
    public int AutoSaveTimeGroupIndex;


    public ReadAnyCardSetting_Result() {
    }

    @Override
    public void release() {
        return;
    }
    
}

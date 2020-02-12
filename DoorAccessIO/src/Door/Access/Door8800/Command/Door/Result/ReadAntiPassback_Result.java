/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Door.Access.Door8800.Command.Door.Result;

import Door.Access.Command.INCommandResult;

/**
 * 防潜返<br/>
 * 刷卡进门后，必须刷卡出门才能再次刷卡进门。<br/>
 *
 * @author 赖金杰
 */
public class ReadAntiPassback_Result implements INCommandResult {

    /**
     * 门号<br/>
     * 门端口在控制板中的索引号，取值：1-4
     */
    public int DoorNum;

    /**
     * 是否启用防潜返功能
     */
    public boolean Use;

    public ReadAntiPassback_Result() {
    }

    @Override
    public void release() {
        return;
    }

}

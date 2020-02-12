/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Door.Access.Door8800.Command.System.Parameter;

import Door.Access.Command.CommandDetail;
import Door.Access.Command.CommandParameter;
import Door.Access.Door8800.Command.Data.DoorLimit;

/**
 * 门内人数上限参数
 *
 * @author 赖金杰
 */
public class WriteEnterDoorLimit_Parameter extends CommandParameter {

    /**
     * 门内人数限制.<br/>
     * 上限值：0--表示不受限制.<br/>
     * 全局上限优先级最高，全局上限如果大于 0 则设备使用全局上限.<br/>
     * 例如：<br/>
     * 全局上限为100,1门上限为50,2门上限为100,。。。。4门上限为1000 <br/>
     * 设备将使用全局上限100，即整个主板上进入数不能超过100。<br/>
     * 此数据重启后清空。
     */
    public DoorLimit Limit;

    public WriteEnterDoorLimit_Parameter(CommandDetail detail) {
        super(detail);
    }

}

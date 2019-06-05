/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Net.PC15.FC8800.Command.Door.Result;

import Net.PC15.Command.INCommandResult;
import Net.PC15.FC8800.Command.Data.DoorPortDetail;

/**
 * 读取控制器4个门继电器参数
 *
 * @author 赖金杰
 */
public class ReadRelayOption_Result implements INCommandResult {

    /**
     * 继电器参数<br/>
     * <ul>
     * <li>1 &emsp; 不输出（默认） COM & NC                                                     </li>
     * <li>2 &emsp; 输出 COM & NO                                                     </li>
     * <li>3 &emsp; 读卡切换输出状态（当读到合法卡后自动自动切换到当前相反的状态。）例如卷闸门。 </li>
     * </ul><br/>
     */
    public DoorPortDetail door;

    public ReadRelayOption_Result() {
        door = new DoorPortDetail((short) 4);
    }

    @Override
    public void release() {
        return;
    }

}

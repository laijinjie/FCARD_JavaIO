/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Net.PC15.FC8800.Command.Door.Parameter;

import Net.PC15.Command.CommandDetail;
import Net.PC15.Command.CommandParameter;
import Net.PC15.FC8800.Command.Data.DoorPortDetail;

/**
 * 控制器4个门的读卡器字节数
 *
 * @author 赖金杰
 */
public class WriteReaderOption_Parameter extends CommandParameter {

    /**
     * 读卡器字节数<br/>
     * <ul>
     * <li>1 &emsp; 韦根26(三字节)</li>
     * <li>2 &emsp; 韦根34(四字节)</li>
     * <li>3 &emsp; 韦根26(二字节)</li>
     * <li>4 &emsp; 禁用          </li>
     * </ul><br/>
     */
    public DoorPortDetail door;

    public WriteReaderOption_Parameter(CommandDetail detail) {
        super(detail);
        door = new DoorPortDetail((short) 4);
    }

}

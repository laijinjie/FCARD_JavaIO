/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Net.PC15.FC8800.Command.Door.Result;

import Net.PC15.Command.INCommandResult;
import Net.PC15.FC8800.Command.Data.DoorPortDetail;

/**
 * 读取控制器4个门的读卡器字节数<br/>
 *
 * @author 赖金杰
 */
public class ReadReaderOption_Result implements INCommandResult {
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

    public ReadReaderOption_Result() {
        door = new DoorPortDetail((short) 4);
    }

    @Override
    public void release() {
        return;
    }
}

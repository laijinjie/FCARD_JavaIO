/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Net.PC15.FC8800.Command.System.Parameter;

import Net.PC15.Command.CommandDetail;
import Net.PC15.Command.CommandParameter;

/**
 * 读卡器密码键盘启用功能开关
 *
 * @author 赖金杰
 */
public class WriteKeyboard_Parameter extends CommandParameter {

    /**
     * 读卡器密码键盘启用功能开关.<br/>
     *
     * <ul>
     * <li>Bit0 &emsp; 1号读头</li>
     * <li>Bit1 &emsp; 2号读头</li>
     * <li>Bit2 &emsp; 3号读头</li>
     * <li>Bit3 &emsp; 4号读头</li>
     * <li>Bit4 &emsp; 5号读头</li>
     * <li>Bit5 &emsp; 6号读头</li>
     * <li>Bit6 &emsp; 7号读头</li>
     * <li>Bit7 &emsp; 8号读头</li>
     * </ul>
     */
    public short Keyboard;

    public WriteKeyboard_Parameter(CommandDetail detail) {
        super(detail);
    }

}

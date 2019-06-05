/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Net.PC15.FC8800.Command.System.Parameter;

import Net.PC15.Command.CommandDetail;
import Net.PC15.Command.CommandParameter;

/**
 * 读卡器校验参数
 *
 * @author 赖金杰
 */
public class WriteReaderCheckMode_Parameter extends CommandParameter {

    /**
     * 读卡器校验.<br/>
     * 0不启用，1启用，2启用校验，但不提示非法数据或线路异常。
     */
    public short ReaderCheckMode;

    public WriteReaderCheckMode_Parameter(CommandDetail detail) {
        super(detail);
    }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Net.PC15.FC8800.Command.System.Parameter;

import Net.PC15.Command.CommandDetail;
import Net.PC15.Command.CommandParameter;

/**
 * 记录存储方式.
 *
 * @author 赖金杰
 */
public class WriteRecordMode_Parameter extends CommandParameter {

    /**
     * 记录存储方式.<br/>
     * 0是满循环，1表示满不循环
     */
    public short Mode;

    public WriteRecordMode_Parameter(CommandDetail detail) {
        super(detail);
    }

}

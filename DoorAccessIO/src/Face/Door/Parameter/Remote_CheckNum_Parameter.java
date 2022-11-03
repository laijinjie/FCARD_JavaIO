/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Face.Door.Parameter;

import Door.Access.Command.CommandDetail;
import Door.Access.Command.CommandParameter;

/**
 * 远程开门_带验证码
 *
 * @author F
 */
public class Remote_CheckNum_Parameter extends CommandParameter {
    int MAX_VALUE = 254;
    int MIN_VALUE = 1;
    /**
     * 验证码
     */
    public byte CheckNum;
/**
 * 远程开门_带验证码
 * @param detail 命令详情
 * @param checkNum 验证码
 */
    public Remote_CheckNum_Parameter(CommandDetail detail, byte checkNum) {
        super(detail);
        CheckNum = checkNum;
        checkedParameter();
    }

    private boolean checkedParameter() {
        if (CheckNum < MIN_VALUE || CheckNum > MAX_VALUE) {
            throw new UnsupportedOperationException(String.format("CheckNum must between %s and %s!", MIN_VALUE, MAX_VALUE));
        }
        return true;
    }
}

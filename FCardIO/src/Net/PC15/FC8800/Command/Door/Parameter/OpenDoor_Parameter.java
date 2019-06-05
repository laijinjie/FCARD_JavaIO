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
 * 远程开门指令的参数
 * @author 赖金杰
 */
public class OpenDoor_Parameter extends CommandParameter {

    /**
     * 指定远程开门的门信息<br/>
     * 值为1表示开门，0表示不开门
     */
    public DoorPortDetail Door;

    /**
     * 开门指令是否带有验证序号
     */
    public boolean IsCheckNum;
    /**
     * 开门指令附带的验证序号
     */
    public int CheckNum;

    public OpenDoor_Parameter(CommandDetail detail) {
        super(detail);
        Door = new DoorPortDetail((short) 4);
        IsCheckNum = false;
        CheckNum = 1;
    }

}

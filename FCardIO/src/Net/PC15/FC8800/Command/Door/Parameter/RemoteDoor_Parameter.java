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
 * 执行远程操作时的门参数<br/>
 * 远程关门、远程锁定，远程常开，远程解锁
 *
 * @author 赖金杰
 */
public class RemoteDoor_Parameter extends CommandParameter {

    /**
     * 指定远程操作的门参数<br/>
     * 门的值为1表示操作，0表示不操作
     */
    public DoorPortDetail Door;

    public RemoteDoor_Parameter(CommandDetail detail) {
        super(detail);
        Door = new DoorPortDetail((short) 4);

    }

}

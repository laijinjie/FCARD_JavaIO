/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Net.PC15.FC8800.Command.System.Parameter;

import Net.PC15.Command.CommandDetail;
import Net.PC15.Command.CommandParameter;
import Net.PC15.FC8800.Command.Data.DoorPortDetail;
import Net.PC15.FC8800.Command.FC8800Command;

/**
 * 互锁功能开关
 * @author 赖金杰
 */
public class WriteLockInteraction_Parameter extends CommandParameter {
    /**
     * 互锁功能开关.<br/>
     * 4个门的互锁状态，各门端口的取值：1--已启用互锁功能，0--不启用互锁功能
     */
    public final DoorPortDetail DoorPort;

    public WriteLockInteraction_Parameter(CommandDetail detail) {
        super(detail);
        DoorPort = new DoorPortDetail(FC8800Command.DoorMax);
    }
}

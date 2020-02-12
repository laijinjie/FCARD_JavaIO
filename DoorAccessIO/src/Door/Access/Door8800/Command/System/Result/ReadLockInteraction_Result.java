/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Door.Access.Door8800.Command.System.Result;

import Door.Access.Command.INCommandResult;
import Door.Access.Door8800.Command.Data.DoorPortDetail;
import Door.Access.Door8800.Command.Door8800Command;

/**
 * 互锁功能开关
 * @author 赖金杰
 */
public class ReadLockInteraction_Result implements INCommandResult{
    /**
     * 互锁功能开关.<br/>
     * 4个门的互锁状态，各门端口的取值：1--已启用互锁功能，0--不启用互锁功能
     */
    public final DoorPortDetail DoorPort;
    
    public ReadLockInteraction_Result()
    {
        DoorPort = new DoorPortDetail(Door8800Command.DoorMax);
    }

    @Override
    public void release() {
        return;
    }
    
}

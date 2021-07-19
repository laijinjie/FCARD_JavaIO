/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Door.Access.Door8800.Command.Data;

import Door.Access.Data.AbstractTransaction;
import Door.Access.Util.TimeUtil;
import io.netty.buffer.ByteBuf;

/**
 * 门磁记录<br>
 * TransactionCode 事件代码含义表：<br>
 * <ul>
 * <li>1 &emsp; 开门               </li>
 * <li>2 &emsp; 关门               </li>
 * <li>3 &emsp; 进入门磁报警状态   </li>
 * <li>4 &emsp; 退出门磁报警状态   </li>
 * <li>5 &emsp; 门未关好           </li>
 * </ul>
 *
 * @author 赖金杰
 */
public class DoorSensorTransaction extends AbstractDoorTransaction {

    public DoorSensorTransaction() {
        super(3);
    }

}

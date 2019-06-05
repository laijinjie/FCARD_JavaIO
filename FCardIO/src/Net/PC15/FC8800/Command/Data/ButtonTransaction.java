/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Net.PC15.FC8800.Command.Data;

import Net.PC15.Data.AbstractTransaction;
import Net.PC15.Util.TimeUtil;
import io.netty.buffer.ByteBuf;

/**
 * 出门按钮记录<br/>
 * TransactionCode 事件代码含义表：<br/>
 * <ul>
 * <li>1 &emsp; 合法开门            </li>
 * <li>2 &emsp; 开门时段过期        </li>
 * <li>3 &emsp; 锁定时按钮          </li>
 * <li>4 &emsp; 控制器已过期        </li>
 * <li>5 &emsp; 互锁时按钮(不开门)  </li>
 * </ul>
 *
 * @author 赖金杰
 */
public class ButtonTransaction extends AbstractDoorTransaction {

    public ButtonTransaction() {
        super(2);
    }
}

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
 * 软件操作记录<br/>
 * TransactionCode 事件代码含义表：<br/>
 * <ul>
 * <li>1 &emsp; 软件开门                             </li>
 * <li>2 &emsp; 软件关门                             </li>
 * <li>3 &emsp; 软件常开                             </li>
 * <li>4 &emsp; 控制器自动进入常开                   </li>
 * <li>5 &emsp; 控制器自动关闭门                     </li>
 * <li>6 &emsp; 长按出门按钮常开                     </li>
 * <li>7 &emsp; 长按出门按钮常闭                     </li>
 * <li>8 &emsp; 软件锁定                             </li>
 * <li>9 &emsp; 软件解除锁定                         </li>
 * <li>10 &emsp; 控制器定时锁定--到时间自动锁定      </li>
 * <li>11 &emsp; 控制器定时锁定--到时间自动解除锁定  </li>
 * <li>12 &emsp; 报警--锁定                          </li>
 * <li>13 &emsp; 报警--解除锁定                      </li>
 * <li>14 &emsp; 互锁时远程开门                      </li>
 * </ul>
 *
 * @author 赖金杰
 */
public class SoftwareTransaction extends AbstractDoorTransaction {

    public SoftwareTransaction() {
        super(4);
    }

}

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
 * 报警记录<br/>
 * TransactionCode 事件代码含义表：<br/>
 * <ul>
 * <li>1 &emsp; 门磁报警                      </li>
 * <li>2 &emsp; 匪警报警                      </li>
 * <li>3 &emsp; 消防报警                      </li>
 * <li>4 &emsp; 非法卡刷报警                  </li>
 * <li>5 &emsp; 胁迫报警                      </li>
 * <li>6 &emsp; 消防报警(命令通知)            </li>
 * <li>7 &emsp; 烟雾报警                      </li>
 * <li>8 &emsp; 防盗报警                      </li>
 * <li>9 &emsp; 黑名单报警                    </li>
 * <li>10 &emsp; 开门超时报警                 </li>
 * <li>0x11 &emsp; 门磁报警撤销               </li>
 * <li>0x12 &emsp; 匪警报警撤销               </li>
 * <li>0x13 &emsp; 消防报警撤销               </li>
 * <li>0x14 &emsp; 非法卡刷报警撤销           </li>
 * <li>0x15 &emsp; 胁迫报警撤销               </li>
 * <li>0x17 &emsp; 撤销烟雾报警               </li>
 * <li>0x18 &emsp; 关闭防盗报警               </li>
 * <li>0x19 &emsp; 关闭黑名单报警             </li>
 * <li>0x1A &emsp; 关闭开门超时报警           </li>
 * <li>0x21 &emsp; 门磁报警撤销(命令通知)     </li>
 * <li>0x22 &emsp; 匪警报警撤销(命令通知)     </li>
 * <li>0x23 &emsp; 消防报警撤销(命令通知)     </li>
 * <li>0x24 &emsp; 非法卡刷报警撤销(命令通知) </li>
 * <li>0x25 &emsp; 胁迫报警撤销(命令通知)     </li>
 * <li>0x27 &emsp; 撤销烟雾报警(命令通知)     </li>
 * <li>0x28 &emsp; 关闭防盗报警(软件关闭)     </li>
 * <li>0x29 &emsp; 关闭黑名单报警(软件关闭)   </li>
 * <li>0x2A &emsp; 关闭开门超时报警           </li>
 * </ul>
 *
 * @author 赖金杰
 */
public class AlarmTransaction extends AbstractDoorTransaction {

    public AlarmTransaction() {
        super(5);
    }

}

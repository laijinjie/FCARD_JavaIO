/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Door.Access.Door8800.Command.System.Result;

import Door.Access.Command.INCommandResult;
import Door.Access.Door8800.Command.Data.DoorLimit;
import Door.Access.Door8800.Command.Data.DoorPortDetail;

/**
 * 控制器各端口工作状态信息
 *
 * @author 赖金杰
 */
public class ReadWorkStatus_Result implements INCommandResult {

    /**
     * 门继电器物理状态.<br>
     * <ul>
     * <li>0 &emsp; 表示COM和NC常闭                </li>
     * <li>1 &emsp; 表示COM和NO常闭                </li>
     * <li>2 &emsp; 表示继电器无法检测，继电器异常 </li>
     * </ul>
     */
    public DoorPortDetail RelayState;
    /**
     * 运行状态.<br>
     * 常开还是常闭，0表示常闭，1常开
     */
    public DoorPortDetail DoorLongOpenState;
    /**
     * 门磁开关.<br>
     * 0表关，1表示开
     */
    public DoorPortDetail DoorState;
    /**
     * 门报警状态.<br>
     * <ul>
     * <li>0 &emsp; 非法刷卡报警 </li>
     * <li>1 &emsp; 门磁报警     </li>
     * <li>2 &emsp; 胁迫报警     </li>
     * <li>3 &emsp; 开门超时报警 </li>
     * <li>4 &emsp; 黑名单报警   </li>
     * </ul>
     */
    public DoorPortDetail DoorAlarmState;

    /**
     * 设备报警状态.<br>
     * <ul>
     * <li>0 &emsp; 匪警报警           </li>
     * <li>1 &emsp; 防盗报警           </li>
     * <li>2 &emsp; 消防报警           </li>
     * <li>3 &emsp; 烟雾报警           </li>
     * <li>4 &emsp; 消防报警(命令通知) </li>
     * </ul>
     */
    public int AlarmState;

    /**
     * 继电器逻辑状态.<br>
     * <h2>继电器的逻辑开关状态</h2>
     * <ul>
     * <li>0--继电器关；</li>
     * <li>1--继电器开；</li>
     * <li>2--双稳态；  </li>
     * </ul>
     * <h2>门序号值说明</h2>
     * 1-4是表示门的继电器，这个继电器状态需要根据门的继电器类型判断真实物理状况或者根据第一组状态值【门继电器物理状态】判断。<br>
     * 5-8是报警继电器，目前定义只有0或1两个状态。<br>
     * 状态0表示：COM和NC导通<br>
     * 状态1表示：COM和NO导通<br>
     * <ul>
     * <li>1-4 &emsp; 4个门的继电器     </li>
     * <li>5 &emsp; 消防继电器          </li>
     * <li>6 &emsp; 匪警继电器          </li>
     * <li>7 &emsp; 烟雾报警继电器      </li>
     * <li>8 &emsp; 防盗主机报警继电器  </li>
     * </ul>
     */
    public DoorPortDetail LockState;

    /**
     * 锁定状态.<br>
     * 4个门，0--未锁定，1--锁定
     */
    public DoorPortDetail PortLockState;

    /**
     * 监控状态.<br>
     * 0--未开启监控；1--开启监控
     */
    public int WatchState;

    /**
     * 门内人数.
     */
    public DoorLimit EnterTotal;

    /**
     * 防盗主机布防状态.<br>
     * <ul>
     * <li>1 &emsp; 延时布防              </li>
     * <li>2 &emsp; 已布防                </li>
     * <li>3 &emsp; 延时撤防              </li>
     * <li>4 &emsp; 未布防                </li>
     * <li>5 &emsp; 报警延时，准备启用报警</li>
     * <li>6 &emsp; 防盗报警已启动        </li>
     * </ul>
     */
    public int TheftState;

    @Override
    public void release() {
        return;
    }

}

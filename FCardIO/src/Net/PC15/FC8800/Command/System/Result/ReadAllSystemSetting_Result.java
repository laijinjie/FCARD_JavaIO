/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Net.PC15.FC8800.Command.System.Result;

import Net.PC15.Command.INCommandResult;
import Net.PC15.FC8800.Command.Data.BroadcastDetail;
import Net.PC15.FC8800.Command.Data.DoorLimit;
import Net.PC15.FC8800.Command.Data.DoorPortDetail;
import Net.PC15.FC8800.Command.Data.ReadCardSpeak;
import Net.PC15.FC8800.Command.Data.TheftAlarmSetting;

/**
 * 读取所有系统参数的返回值
 *
 * @author 赖金杰
 */
public class ReadAllSystemSetting_Result implements INCommandResult {

    /**
     * 记录存储方式.<br/>
     * 0是满循环，1表示满不循环
     */
    public short RecordMode;
    /**
     * 读卡器密码键盘启用功能开关.<br/>
     *
     * <ul>
     * <li>Bit0 &emsp; 1号读头</li>
     * <li>Bit1 &emsp; 2号读头</li>
     * <li>Bit2 &emsp; 3号读头</li>
     * <li>Bit3 &emsp; 4号读头</li>
     * <li>Bit4 &emsp; 5号读头</li>
     * <li>Bit5 &emsp; 6号读头</li>
     * <li>Bit6 &emsp; 7号读头</li>
     * <li>Bit7 &emsp; 8号读头</li>
     * </ul>
     */
    public short Keyboard;
    /**
     * 互锁功能开关.<br/>
     * 4个门的互锁状态，各门端口的取值：1--已启用互锁功能，0--不启用互锁功能
     */
    public DoorPortDetail LockInteraction;
    /**
     * 消防报警功能参数.<br/>
     * <ul>
     * <li>值 &emsp; 解释                                   </li>
     * <li>0 &emsp; 不启用                                 </li>
     * <li>1 &emsp; 报警输出，并开所有门，只能软件解除     </li>
     * <li>2 &emsp; 报警输出，不开所有门，只能软件解除     </li>
     * <li>3 &emsp; 有信号报警并开门，无信号解除报警并关门 </li>
     * <li>4 &emsp; 有报警信号时开一次门，就像按钮开门一样 </li>
     * </ul>
     */
    public short FireAlarmOption;
    /**
     * 匪警报警功能参数.<br/>
     * <ul>
     * <li>值 &emsp; 解释                                                                                                                                                 </li>
     * <li>0 &emsp; 关闭此功能                                                                                                                                            </li>
     * <li>1 &emsp; 所有门锁定，报警输出，蜂鸣器不响。不开门，刷卡不能解除，软件解除，解除报警后门的锁定也解锁了。                                                        </li>
     * <li>2 &emsp; 报警输出，不锁定，蜂鸣器响。不开门，刷卡可以解除，软件可以解除                                                                                        </li>
     * <li>3 &emsp;
     * 按报警按钮就报警，门锁定，并输出，不按时就停止。不开门，按钮停止时就解除，软件或刷卡不能解除。按报警按钮的时候门是处于锁定状态的，不按时解除锁定状态。</li>
     * </ul>
     */
    public short OpenAlarmOption;
    /**
     * 读卡间隔时间. <br/>
     * 最大65535秒。0表示无限制
     */
    public int ReaderIntervalTime;
    /**
     * 语音播报开关.<br/>
     * 语音段对照可参考《FC8800语音表》 每个开关true 表示启用，false 表示禁用
     */
    public BroadcastDetail SpeakOpen;
    /**
     * 读卡器校验.<br/>
     * 0不启用，1启用，2启用校验，但不提示非法数据或线路异常。
     */
    public short ReaderCheckMode;
    /**
     * 主板蜂鸣器.<br/>
     * 0不启用，1启用。
     */
    public short BuzzerMode;
    /**
     * 烟雾报警功能参数.<br/>
     * <ul>
     * <li>值 &emsp; 解释                                                                    </li>
     * <li>0 &emsp; 关闭此功能（默认）                                                                  </li>
     * <li>1 &emsp; 驱动 [烟雾报警继电器]，(信号有，就驱动的，信号无，就关闭)                           </li>
     * <li>2 &emsp; 驱动烟雾报警继电器并驱动所有门继电器，主板报警提示音响(开启后由软件关闭，或重启。)  </li>
     * <li>3 &emsp; 驱动烟雾报警继电器并锁定所有门，主板报警提示音响(开启后由软件关闭，或重启。)        </li>
     * </ul>
     */
    public short SmogAlarmOption;
    /**
     * 门内人数限制.<br/>
     * 上限值：0--表示不受限制.<br/>
     * 全局上限优先级最高，全局上限如果大于 0 则设备使用全局上限.<br/>
     * 例如：<br/>
     * 全局上限为100,1门上限为50,2门上限为100,。。。。4门上限为1000 <br/>
     * 设备将使用全局上限100，即整个主板上进入数不能超过100。<br/>
     * 此数据重启后清空。
     */
    public DoorLimit EnterDoorLimit;
    /**
     * 防盗主机.
     */
    public TheftAlarmSetting TheftAlarmPar;
    /**
     * 防潜回功能参数.<br/>
     * 01--单独每个门检测防潜回；02--整个控制器统一防潜回
     */
    public short CheckInOut;
    /**
     * 卡片到期提示.<br/>
     * 0不启用，1启用
     */
    public short CardPeriodSpeak;
    /**
     * 定时播报.
     */
    public ReadCardSpeak ReadCardSpeak;

    @Override
    public void release() {
        return;
    }

}

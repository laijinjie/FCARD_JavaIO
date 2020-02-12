/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Door.Access.Door8800.Command.System.Result;

import Door.Access.Command.INCommandResult;

/**
 * 烟雾报警功能参数
 *
 * @author 赖金杰
 */
public class ReadSmogAlarmOption_Result implements INCommandResult {

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
    public short Option;

    @Override
    public void release() {
        return;
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Door.Access.Door8800.Command.Door.Result;

import Door.Access.Command.INCommandResult;
import Door.Access.Util.StringUtil;

/**
 * 胁迫报警功能<br/>
 * 功能开启后，在密码键盘读卡器上输入特定密码后就会报警；<br/>
 *
 * @author 赖金杰
 */
public class ReadAlarmPassword_Result implements INCommandResult {


    /**
     * 门号<br/>
     * 门端口在控制板中的索引号，取值：1-4
     */
    public int DoorNum;

    /**
     * 是否启用胁迫报警功能
     */
    public boolean Use;

    /**
     * 胁迫报警密码，最大长度8个字符，由数字组成。
     */
    public String Password;

    /**
     * 报警选项<br/>
     * <ul>
     * <li>1 &emsp; 不开门，报警输出           </li>
     * <li>2 &emsp; 开门，报警输出             </li>
     * <li>3 &emsp; 锁定门，报警，只能软件解锁 </li>
     * </ul>
     */
    public int AlarmOption;

    public ReadAlarmPassword_Result() {
    }

    @Override
    public void release() {
        return;
    }
}

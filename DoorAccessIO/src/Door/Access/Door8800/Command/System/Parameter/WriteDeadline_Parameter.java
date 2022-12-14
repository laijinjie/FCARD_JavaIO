/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Door.Access.Door8800.Command.System.Parameter;

import Door.Access.Command.CommandDetail;
import Door.Access.Command.CommandParameter;
import Door.Access.Util.UInt32Util;

/**
 * 设置控制器剩余天数，在有效天数内所有功能一切正常。
 *
 * @author 赖金杰
 */
public class WriteDeadline_Parameter extends CommandParameter {

    private int _Deadline;

    public WriteDeadline_Parameter(CommandDetail detail, int Deadline) {
        super(detail);
        SetDeadline(Deadline);
    }

    public int GetDeadline() {
        return _Deadline;
    }

    public void SetDeadline(int Deadline) {
        if (!UInt32Util.CheckNum(Deadline, 0, 65535)) {
            _Deadline = 0;
        } else {
            _Deadline = Deadline;
        }

    }

}

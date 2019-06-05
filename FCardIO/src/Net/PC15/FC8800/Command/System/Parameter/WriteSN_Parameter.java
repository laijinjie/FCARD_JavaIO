/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Net.PC15.FC8800.Command.System.Parameter;

import Net.PC15.Command.CommandDetail;
import Net.PC15.Command.CommandParameter;
import Net.PC15.Util.StringUtil;

/**
 * 即将写入控制器的SN
 *
 * @author 赖金杰
 */
public class WriteSN_Parameter extends CommandParameter {

    private String _SN;

    public WriteSN_Parameter(CommandDetail detail, String SN) {
        super(detail);
        SetSN(SN);
    }

    /**
     * 获取控制器SN
     *
     * @return 获取控制器SN
     */
    public String GetSN() {
        return _SN;
    }

    /**
     * 设置控制器SN
     *
     * @return
     */
    public void SetSN(String sn) {
        if (StringUtil.IsNullOrEmpty(sn)) {
            return;
        }
        if (!StringUtil.IsAscii(sn)) {
            return;
        }
        if (sn.length() != 16) {
            return;
        }
        _SN = sn;
    }
}

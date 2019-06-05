/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Net.PC15.FC8800.Command.System.Parameter;

import Net.PC15.Command.CommandDetail;
import Net.PC15.Command.CommandParameter;

/**
 * 设置网络标记
 * @author 赖金杰
 */
public class SearchEquptOnNetNum_Parameter extends CommandParameter {
    /**
     * 网络标记 取值范围：1-65535
     */
    public int NetNum;

    public SearchEquptOnNetNum_Parameter(CommandDetail detail,int num) {
        super(detail);
        NetNum = num;
    }
    
}

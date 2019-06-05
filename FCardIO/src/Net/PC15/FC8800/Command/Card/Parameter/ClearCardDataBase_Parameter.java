/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Net.PC15.FC8800.Command.Card.Parameter;

import Net.PC15.Command.CommandDetail;
import Net.PC15.Command.CommandParameter;

/**
 * 清空卡片数据库
 *
 * @author 赖金杰
 */
public class ClearCardDataBase_Parameter extends CommandParameter {

    /**
     * 待清空的卡片数据库类型<br/>
     * <ul>
     * <li>1 &emsp; 排序卡区域   </li>
     * <li>2 &emsp; 非排序卡区域 </li>
     * <li>3 &emsp; 所有区域     </li>
     * </ul>
     */
    public int CardType;

    public ClearCardDataBase_Parameter(CommandDetail detail, int type) {
        super(detail);
        this.CardType = type;
    }

}

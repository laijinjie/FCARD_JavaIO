/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Door.Access.Door8800.Command.Card.Parameter;

import Door.Access.Command.CommandDetail;
import Door.Access.Command.CommandParameter;

/**
 * 读取卡片数据库中的所有卡数据
 * @author 赖金杰
 */
public class ReadCardDataBase_Parameter  extends CommandParameter {

    /**
     * 待读取的卡片数据库类型<br/>
     * <ul>
     * <li>1 &emsp; 排序卡区域   </li>
     * <li>2 &emsp; 非排序卡区域 </li>
     * <li>3 &emsp; 所有区域     </li>
     * </ul>
     */
    public int CardType;

    public ReadCardDataBase_Parameter(CommandDetail detail, int type) {
        super(detail);
        this.CardType = type;
    }
    
}

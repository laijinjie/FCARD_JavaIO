/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Net.PC15.FC89H.Command.Card.Parameter;

import Net.PC15.Command.CommandDetail;
import Net.PC15.Command.CommandParameter;

/**
 * 针对FC89H使用，读取单个卡片在控制器中的信息<br/>
 * @author 徐铭康
 */
public class DeleteCard_Parameter extends CommandParameter {
    
    /**
     * 要读取详情的授权卡卡号<br/>
     * 取值：1-0xFFFFFFFF
     */
    public String[] CardList;
    
    public DeleteCard_Parameter(CommandDetail detail, String[] list) {
        super(detail);
        this.CardList = list;
    }
    
}

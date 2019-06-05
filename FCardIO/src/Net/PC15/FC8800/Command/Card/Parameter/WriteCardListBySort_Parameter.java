/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Net.PC15.FC8800.Command.Card.Parameter;

import Net.PC15.Command.CommandDetail;
import Net.PC15.Command.CommandParameter;
import Net.PC15.FC8800.Command.Data.CardDetail;
import java.util.ArrayList;
import java.util.Collections;

/**
 *
 * @author 赖金杰
 */
public class WriteCardListBySort_Parameter extends CommandParameter {

    /**
     * 需要上传的卡片列表
     */
    public ArrayList<CardDetail> CardList;

    public WriteCardListBySort_Parameter(CommandDetail detail, ArrayList<CardDetail> list) {
        super(detail);
        CardList = list;
        if (CardList != null) {
            Collections.sort(CardList);
        }
    }
    
}

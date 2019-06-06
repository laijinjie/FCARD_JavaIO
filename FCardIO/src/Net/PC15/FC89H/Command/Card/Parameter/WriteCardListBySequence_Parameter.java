/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Net.PC15.FC89H.Command.Card.Parameter;

import Net.PC15.Command.CommandDetail;
import Net.PC15.Command.CommandParameter;
import Net.PC15.FC89H.Command.Data.CardDetail;
import java.util.ArrayList;
import java.util.Collections;

/**
 * 针对FC89H使用，将卡片列表写入到控制器非排序区
 *
 * @author 徐铭康
 */
public class WriteCardListBySequence_Parameter extends CommandParameter {

    /**
     * 需要上传的卡片列表
     */
    public ArrayList<CardDetail> CardList;

    public WriteCardListBySequence_Parameter(CommandDetail detail, ArrayList<CardDetail> list) {
        super(detail);
        CardList = list;
        if (CardList != null) {
            Collections.sort(CardList);
        }
    }

}

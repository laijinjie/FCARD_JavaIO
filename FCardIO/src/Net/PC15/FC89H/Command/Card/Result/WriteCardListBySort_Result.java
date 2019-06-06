/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Net.PC15.FC89H.Command.Card.Result;

import Net.PC15.Command.INCommandResult;
import Net.PC15.FC89H.Command.Data.CardDetail;
import java.util.ArrayList;

/**
 *
 * @author 徐铭康
 */
public class WriteCardListBySort_Result implements INCommandResult {

    /**
     * 失败卡数量.
     */
    public int FailTotal;
    
    /**
     * 失败的卡列表
     */
    public ArrayList<CardDetail> CardList;

    public WriteCardListBySort_Result() {
        
    }

    @Override
    public void release() {
        return;
    }
    
}

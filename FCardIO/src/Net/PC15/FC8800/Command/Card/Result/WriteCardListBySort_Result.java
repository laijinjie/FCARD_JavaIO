/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Net.PC15.FC8800.Command.Card.Result;

import Net.PC15.Command.INCommandResult;
import Net.PC15.FC8800.Command.Data.CardDetail;
import java.util.ArrayList;

/**
 *
 * @author 赖金杰
 */
public class WriteCardListBySort_Result implements INCommandResult {

    /**
     * 失败卡数量.
     */
    public int FailTotal;
    
    /**
     * 失败的卡列表
     */
    public ArrayList<? extends CardDetail> CardList;

    public WriteCardListBySort_Result() {
        
    }

    @Override
    public void release() {
        return;
    }
    
}

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
 * FC89H,读取卡片数据
 * @author 徐铭康

public class ReadCardDataBase_Result implements INCommandResult {

    
    public ArrayList<CardDetail> CardList;

    public int DataBaseSize;
    
    
    public int CardType;
    

    public ReadCardDataBase_Result(int type) {
        DataBaseSize = 0;
        CardList = null;
        CardType =type;
    }

    @Override
    public void release() {
        CardList.clear();
        CardList = null;
        return;
    }

}
 */
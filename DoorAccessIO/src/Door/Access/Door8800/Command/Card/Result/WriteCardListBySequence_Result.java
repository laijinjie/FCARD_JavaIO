/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Door.Access.Door8800.Command.Card.Result;

import Door.Access.Command.INCommandResult;
import Door.Access.Door8800.Command.Data.CardDetail;
import java.util.ArrayList;

/**
 * 将卡片列表写入到控制器非排序区
 *
 * @author 赖金杰
 */
public class WriteCardListBySequence_Result implements INCommandResult {

    /**
     * 失败卡数量.
     */
    public int FailTotal;
    
    /**
     * 失败的卡列表
     */
    public ArrayList<? extends CardDetail> CardList;

    public long OverflowCount;

    public WriteCardListBySequence_Result() {
        
    }

    @Override
    public void release() {
        return;
    }

}

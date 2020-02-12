/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Door.Access.Door8800.Command.Card.Parameter;

import Door.Access.Command.CommandDetail;
import Door.Access.Command.CommandParameter;
import Door.Access.Door8800.Command.Data.CardDetail;
import java.util.ArrayList;
import java.util.Collections;

/**
 * 将卡片列表写入到控制器非排序区
 *
 * @author 赖金杰
 */
public class WriteCardListBySequence_Parameter extends CommandParameter {

    /**
     * 需要上传的卡片列表
     */
    public ArrayList<? extends CardDetail> CardList;

    public WriteCardListBySequence_Parameter(CommandDetail detail, ArrayList<? extends CardDetail> list) {
        super(detail);
        CardList = list;
        if (CardList != null) {
            Collections.sort(CardList);
        }
    }

}

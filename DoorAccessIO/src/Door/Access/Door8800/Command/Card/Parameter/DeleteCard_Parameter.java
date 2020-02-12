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
 * 删除卡片
 *
 * @author 赖金杰
 */
public class DeleteCard_Parameter extends CommandParameter {

    /**
     * 需要删除的卡片列表
     */
    public String[] CardList;

    public DeleteCard_Parameter(CommandDetail detail, String[] list) {
        super(detail);
        CardList = list;
    }

}

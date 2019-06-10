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
 * 删除卡片
 *
 * @author 赖金杰
 */
public class DeleteCard_Parameter<T> extends CommandParameter {

    /**
     * 需要删除的卡片列表
     */
    public ArrayList<T> CardList;

    public DeleteCard_Parameter(CommandDetail detail, ArrayList<T> list) {
        super(detail);
        CardList = list;
    }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Net.PC15.FC8800.Command.Card.Result;

import Net.PC15.Command.INCommandResult;
import Net.PC15.FC8800.Command.Data.CardDetail;

/**
 * 读取单个卡片在控制器中的信息
 *
 * @author 赖金杰
 */
public class ReadCardDetail_Result implements INCommandResult {

    /**
     * 卡片是否存在
     */
    public boolean IsReady;

    /**
     * 卡片的详情
     */
    public CardDetail Card;

    public ReadCardDetail_Result() {
    }

    @Override
    public void release() {
        Card = null;
        return;
    }

}

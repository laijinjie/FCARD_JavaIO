/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Net.PC15.FC8800.Command.Card.Parameter;
import Net.PC15.Command.CommandDetail;
import Net.PC15.Command.CommandParameter;

/**
 * 读取单个卡片在控制器中的信息<br/>
 * @author 赖金杰
 */
public class ReadCardDetail_Parameter<T> extends CommandParameter {

    /**
     * 要读取详情的授权卡卡号<br/>
     * 取值：1-0xFFFFFFFF
     */
    public T CardData;


    public ReadCardDetail_Parameter(CommandDetail detail, T data) {
        super(detail);
        this.CardData = data;
    }
    
}

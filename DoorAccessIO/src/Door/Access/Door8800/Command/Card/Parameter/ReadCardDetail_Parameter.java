/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Door.Access.Door8800.Command.Card.Parameter;
import Door.Access.Command.CommandDetail;
import Door.Access.Command.CommandParameter;
import java.math.BigInteger;

/**
 * 读取单个卡片在控制器中的信息<br>
 * @author 赖金杰
 */
public class ReadCardDetail_Parameter extends CommandParameter {

    /**
     * 要读取详情的授权卡卡号<br>
     * 取值：1-0xFFFFFFFF
     */
    public BigInteger CardData;


    public ReadCardDetail_Parameter(CommandDetail detail, String data) {
        super(detail);
        this.CardData = new BigInteger(data);
    }
    
}

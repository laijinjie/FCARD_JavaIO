/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Door.Access.Door8800.Command.Card;
import Door.Access.Door8800.Command.Card.Parameter.ClearCardDataBase_Parameter;
import Door.Access.Door8800.Command.Door8800Command;
import Door.Access.Util.ByteUtil;
import io.netty.buffer.ByteBuf;
/**
 * 清空卡片数据库
 * @author 赖金杰
 */
public class ClearCardDataBase extends Door8800Command {

    public ClearCardDataBase(ClearCardDataBase_Parameter par) {
        _Parameter = par;
        ByteBuf dataBuf = ByteUtil.ALLOCATOR.buffer(1);
        dataBuf.writeByte(par.CardType);
        CreatePacket(7, 2, 0, 1, dataBuf);
    }

    @Override
    protected void Release0() {
    }
    
}

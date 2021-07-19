/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Door.Access.Door8800.Command.Card;

import Door.Access.Connector.INConnectorEvent;
import Door.Access.Door8800.Command.Card.Parameter.ReadCardDetail_Parameter;
import Door.Access.Door8800.Command.Card.Result.ReadCardDetail_Result;
import Door.Access.Door8800.Command.Data.CardDetail;
import Door.Access.Door8800.Command.Door8800Command;
import Door.Access.Door8800.Packet.Door8800PacketModel;
import Door.Access.Util.ByteUtil;
import Door.Access.Util.StringUtil;
import io.netty.buffer.ByteBuf;
import java.math.BigInteger;

/**
 * 读取单个卡片在控制器中的信息<br>
 * 成功返回结果参考 {@link ReadCardDetail_Result}
 *
 * @author 赖金杰
 */
public class ReadCardDetail extends Door8800Command {

      BigInteger MaxCardData=new  BigInteger("FFFFFFFFFF",16);
    public ReadCardDetail(ReadCardDetail_Parameter par) {
        _Parameter = par;
        ByteBuf dataBuf = ByteUtil.ALLOCATOR.buffer(5);
           if(MaxCardData.compareTo(par.CardData)== -1){
            new Exception("CardData Beyond the limit");
        }
         dataBuf.writeByte(0); 
         long biCardData = Long.valueOf(par.CardData.toString());//biCardData是1099511627775
         dataBuf.writeInt((int)biCardData);
         CreatePacket(7, 3, 1, 5, dataBuf);
    }

    @Override
    protected void Release0() {
        return;
    }

    @Override
    protected boolean _CommandStep(INConnectorEvent oEvent, Door8800PacketModel model) {
        if (CheckResponse_Cmd(model, 7, 3, 1, 0x21)) {
            ByteBuf buf = model.GetDatabuff();

            ReadCardDetail_Result r = new ReadCardDetail_Result();
            buf.markReaderIndex();
            if (buf.readUnsignedByte() == 255) {
                //卡片不存在
                r.IsReady = false;
            } else {
                r.IsReady = true;
                buf.resetReaderIndex();
                CardDetail cd = new CardDetail();
                cd.SetBytes(buf);
                r.Card = cd;
            }

            //设定返回值
            _Result = r;

            RaiseCommandCompleteEvent(oEvent);
            return true;
        } else {
            return false;
        }
    }

}

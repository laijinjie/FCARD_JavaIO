/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Door.Access.Door89H.Command.Card;

import Door.Access.Connector.INConnectorEvent;
import Door.Access.Door8800.Command.Card.Parameter.ReadCardDetail_Parameter;
import Door.Access.Door8800.Command.Card.Result.ReadCardDetail_Result;
import Door.Access.Door89H.Command.Data.CardDetail;
import Door.Access.Door8800.Command.Door8800Command;
import Door.Access.Door8800.Packet.Door8800PacketModel;
import Door.Access.Util.ByteUtil;
import io.netty.buffer.ByteBuf;

/**
 * 读取单个卡片在控制器中的信息，针对Door89H使用<br/>
 * 成功返回结果参考 {@link ReadCardDetail_Result}
 *
 * @author 徐铭康
 */
public class ReadCardDetail extends Door8800Command {

    public ReadCardDetail(ReadCardDetail_Parameter par) {
        _Parameter = par;
        ByteBuf dataBuf = ByteUtil.ALLOCATOR.buffer(9);
        dataBuf.writeByte(0);
        //dataBuf.writeInt((int) par.CardData);
        Door.Access.Util.StringUtil.HextoByteBuf(String.valueOf(par.CardData),dataBuf);
        CreatePacket(7, 3, 1, 9, dataBuf);
    }

    @Override
    protected void Release0() {
        return;
    }

    @Override
    protected boolean _CommandStep(INConnectorEvent oEvent, Door8800PacketModel model) {
        if (CheckResponse_Cmd(model, 7, 3, 1, 0x25)) {
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

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Net.PC15.FC89H.Command.Card;

import Net.PC15.Connector.INConnectorEvent;
import Net.PC15.FC8800.Command.Card.Parameter.ReadCardDataBase_Parameter;
import Net.PC15.FC8800.Command.Card.Result.ReadCardDatabaseDetail_Result;
import Net.PC15.FC8800.Command.FC8800Command;
import Net.PC15.FC8800.Command.Card.Result.ReadCardDataBase_Result;
import Net.PC15.FC89H.Command.Data.CardDetail;
import Net.PC15.FC8800.Packet.FC8800PacketModel;
import Net.PC15.Util.ByteUtil;
import io.netty.buffer.ByteBuf;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * 从控制器中读取卡片数据，针对FC89H使用<br/>
 * 成功返回结果参考 {@link ReadCardDataBase_Result}
 *
 * @author 徐铭康
 */
public class ReadCardDataBase extends Net.PC15.FC8800.Command.Card.ReadCardDataBase {
    public ReadCardDataBase(ReadCardDataBase_Parameter par) {
        super(par);
        
    }
    
    /**
     * 分析缓冲中的数据包
     */
     @Override
    protected void Analysis(int iCardSize) throws Exception{
        ReadCardDataBase_Result result = (ReadCardDataBase_Result) _Result;
        result.DataBaseSize = iCardSize;

        ArrayList<CardDetail> CardList = new ArrayList<>(iCardSize);
        result.CardList = CardList;
        //byte bCardBuf[] = new byte[0x21];
        while (mBufs.peek() != null) {
            ByteBuf buf = mBufs.poll();
            iCardSize = buf.readInt();
            //buf.readBytes(bCardBuf,0,0x21);
            if ((buf.capacity() - 4) % 37 == 0) {
                for (int i = 0; i < iCardSize; i++) {
                    CardDetail cd = new CardDetail();
                    cd.SetBytes(buf);
                    CardList.add(cd);
                }
            }
            else {
                buf.release();
                 throw new Exception("数据流长度不正确");
            }
            buf.release();
        }

    }

}

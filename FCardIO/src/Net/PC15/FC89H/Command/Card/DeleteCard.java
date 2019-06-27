/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Net.PC15.FC89H.Command.Card;

import Net.PC15.FC8800.Command.Card.Parameter.DeleteCard_Parameter;
import Net.PC15.FC8800.Packet.FC8800PacketCompile;
import Net.PC15.FC8800.Packet.FC8800PacketModel;
import Net.PC15.Util.ByteUtil;
import io.netty.buffer.ByteBuf;
import java.math.BigInteger;

/**
 * 删除卡片，针对FC89H使用
 * @author 徐铭康
 * @param <T>
 */
public class DeleteCard<T> extends Net.PC15.FC8800.Command.Card.DeleteCard {
    
    /**
     *
     * @param par
     */
    public DeleteCard(DeleteCard_Parameter par) {
        _Parameter = par;
        _List = par.CardList;
        _ProcessMax = _List.size();
        mIndex = 0;
        //初始化缓冲空间
        int iLen = (40 * 9) + 4;
        ByteBuf dataBuf = ByteUtil.ALLOCATOR.buffer(iLen);
        CreatePacket(7, 5, 0, iLen, dataBuf);
        WriteNext();
    }
    
    /**
     * 写入下一个卡号
     */
    protected void WriteNext(){
        int iMaxSize = 40; //每个数据包最大40个卡
        int iSize = 0;
        int iIndex = 0;
        int ListLen = _List.size();
        
        FC8800PacketCompile compile = (FC8800PacketCompile) _Packet;
        FC8800PacketModel p = (FC8800PacketModel) _Packet.GetPacket();
        ByteBuf dataBuf = p.GetDatabuff();
        dataBuf.clear();
        dataBuf.writeInt(iMaxSize);
        for (int i = mIndex; i < ListLen; i++) {
            iIndex = i;
            iSize += 1;
            dataBuf.writeByte(0);
            String cardData = (String)_List.get(iIndex);
            if (cardData == null || cardData.length() == 0 ) {
                System.out.println("ERROR! 卡号不能为空!");
                return;
            }
        
            if (!Net.PC15.Util.StringUtil.CanParseInt(cardData)) {
                System.out.println("ERROR! 卡号不是数字格式!");
                return;
            }
            String maxHex = new BigInteger(cardData,10).toString(16);
            if (maxHex.equals("ffffffffffffffff")) {
                System.out.println("ERROR! 卡号超过最大值!");
                return;
            }
         
         //传16进制
        String CardDataHEX2 = Net.PC15.Util.StringUtil.FillString(maxHex, 16, "0", false);
        Net.PC15.Util.StringUtil.HextoByteBuf(CardDataHEX2,dataBuf);
            //Net.PC15.Util.StringUtil.HextoByteBuf(,dataBuf);
            if (iSize == iMaxSize) {
                break;
            }
        }
        if (iSize != iMaxSize) {
            dataBuf.setInt(0, iSize);
        }
        p.SetDataLen(dataBuf.readableBytes());//重置数据长度
        compile.Compile();//重新编译
        mIndex = iIndex + 1;
        CommandReady();
    }
    
    
}

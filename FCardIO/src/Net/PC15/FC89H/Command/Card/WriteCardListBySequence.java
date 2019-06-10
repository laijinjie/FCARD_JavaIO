/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Net.PC15.FC89H.Command.Card;

import Net.PC15.Connector.INConnectorEvent;
import Net.PC15.FC8800.Command.Card.Result.WriteCardListBySequence_Result;
import Net.PC15.FC8800.Command.Card.Parameter.WriteCardListBySequence_Parameter;
import Net.PC15.FC89H.Command.Data.CardDetail;
import Net.PC15.FC8800.Packet.FC8800PacketCompile;
import Net.PC15.FC8800.Packet.FC8800PacketModel;
import Net.PC15.Util.ByteUtil;
import io.netty.buffer.ByteBuf;
import java.util.ArrayList;

/**
 * 针对FC89H使用，将卡片列表写入到控制器非排序区<br/>
 * 控制器非排序区写入少量卡时效率高<br/>
 * 但是当区域内存储卡片超过5000时，写入卡片效率会逐渐降低<br/>
 * 建议：当写入卡数量大于2000张时，应清空所有卡，然后将所有卡片上传到排序区，应使用{@link WriteCardListBySort}
 * 成功返回结果参考 {@link WriteCardListBySequence_Result}
 *
 * @author 徐铭康
 */
public class WriteCardListBySequence<T extends Comparable<T>> extends Net.PC15.FC8800.Command.Card.WriteCardListBySequence {
    
    public WriteCardListBySequence(WriteCardListBySequence_Parameter par) {
        //super(par);
        _Parameter = par;
        _List = par.CardList;
        _ProcessMax = par.CardList.size();
        mIndex = 0;
         int iLen = (5 * 0x25) + 4;
        ByteBuf dataBuf = ByteUtil.ALLOCATOR.buffer(iLen);
        CreatePacket(7, 4, 0, iLen, dataBuf);
        WriteNext();
    }
    
    /**
     * 写入下一个卡号
     */
    protected void WriteNext() {
        int iMaxSize = 5; //每个数据包最大5个卡
        int iSize = 0;
        int iIndex = 0;
        int ListLen = _List.size();
        
        FC8800PacketCompile compile = (FC8800PacketCompile) _Packet;
        FC8800PacketModel p = (FC8800PacketModel) _Packet.GetPacket();
        ByteBuf dataBuf = p.GetDatabuff();
        dataBuf.clear();
        dataBuf.writeInt(iMaxSize);
        try
        {
            for (int i = mIndex; i < ListLen; i++) {
            iIndex = i;
            iSize += 1;
            CardDetail cd = (CardDetail)_List.get(iIndex);
            cd.GetBytes(dataBuf);
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
        catch (Exception e){
            
        }
    }
    
    /**
     * 分析缓冲区，确定下载失败的卡号
     */
     @Override
    protected void Analysis() {
        if (mBufs == null) {
            return;
        }
        int iCardSize=0;
        ArrayList<CardDetail> CardList = new ArrayList<>(10000);
        
        WriteCardListBySequence_Result r = (WriteCardListBySequence_Result) _Result;
        r.CardList=CardList;
        
        while (mBufs.peek() != null) {
            ByteBuf buf = (ByteBuf)mBufs.poll();
            iCardSize = buf.readInt();
            for (int i = 0; i < iCardSize; i++) {
                CardDetail cd = new CardDetail();
                cd.SetBytes(buf);
                CardList.add(cd);
            }
            buf.release();
        }
        r.FailTotal=CardList.size();
        
    }
    
}

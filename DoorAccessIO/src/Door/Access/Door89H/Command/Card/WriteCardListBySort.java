/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Door.Access.Door89H.Command.Card;

import Door.Access.Door8800.Command.Card.Result.WriteCardListBySort_Result;
import Door.Access.Door8800.Command.Card.Parameter.WriteCardListBySort_Parameter;
import Door.Access.Door89H.Command.Data.CardDetail;
import Door.Access.Door8800.Packet.Door8800PacketCompile;
import Door.Access.Door8800.Packet.Door8800PacketModel;
import Door.Access.Util.ByteUtil;
import io.netty.buffer.ByteBuf;
import java.util.Collections;

/**
 * 针对Door89H使用，将卡片列表写入到控制器排序区<br>
 * 控制器排序区每次写入都会清空重头写，所以添加少量卡(小于100)时应使用{@link WriteCardListBySort}<br>
 * 成功返回结果参考 {@link WriteCardListBySort_Result}
 *
 * @author 徐铭康
 */
public class WriteCardListBySort extends Door.Access.Door8800.Command.Card.WriteCardListBySort {

    public WriteCardListBySort(WriteCardListBySort_Parameter par) {
        super(par);
    }
    
    /**
     * 初始化写卡操作的资源
     */
    @Override
    protected void IniWriteCard() {
        _ProcessStep = 2;
        //初始化缓冲空间
        int iLen = (10 * 0x25) + 8;
        ByteBuf dataBuf = ByteUtil.ALLOCATOR.buffer(iLen);

        CreatePacket(7, 7, 1, iLen, dataBuf);
        mIndex = 0;
        
        Collections.sort(_List);
        WriteNext();
        CommandReady();
        mStep = 3;
    }
    
    /**
     * 写入下一个卡号
     */
    @Override
    protected void WriteNext() {
        int iMaxSize = 10; //每个数据包最大15个卡
        int iSize = 0;
        int iIndex = 0;

        Door8800PacketCompile compile = (Door8800PacketCompile) _Packet;
        Door8800PacketModel p = (Door8800PacketModel) _Packet.GetPacket();
        ByteBuf dataBuf = p.GetDatabuff();
        dataBuf.clear();
        dataBuf.writeInt(mIndex + 1);
        dataBuf.writeInt(iMaxSize);
        try 
        {
        for (int i = mIndex; i < mUploadMax; i++) {
            iIndex = i;
            iSize += 1;
            CardDetail cd = (CardDetail)_List.get(iIndex) ;
            cd.GetBytes(dataBuf);
            if (iSize == iMaxSize) {
                break;
            }
        }
        if (iSize != iMaxSize) {
            dataBuf.setInt(4, iSize);
        }
        p.SetDataLen(dataBuf.readableBytes());//重置数据长度
        compile.Compile();//重新编译
        mIndex = iIndex + 1;
        CommandReady();
        
        }
        catch (Exception e){
            
        }
    }
}

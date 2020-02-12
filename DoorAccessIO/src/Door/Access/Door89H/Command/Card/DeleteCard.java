/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Door.Access.Door89H.Command.Card;

import Door.Access.Door8800.Packet.Door8800PacketCompile;
import Door.Access.Door8800.Packet.Door8800PacketModel;
import Door.Access.Util.ByteUtil;
import io.netty.buffer.ByteBuf;
import Door.Access.Door8800.Command.Card.Parameter.DeleteCard_Parameter;
import Door.Access.Util.StringUtil;
/**
 * 删除卡片，针对Door89H使用
 * @author 徐铭康
 */
public class DeleteCard extends Door.Access.Door8800.Command.Card.DeleteCard {
    
    /**
     *
     * @param par
     */
    public DeleteCard(DeleteCard_Parameter par) {
        _Parameter = par;
        _List = par.CardList;
        _ProcessMax = _List.length;
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
        int ListLen = _List.length;
        
        Door8800PacketCompile compile = (Door8800PacketCompile) _Packet;
        Door8800PacketModel p = (Door8800PacketModel) _Packet.GetPacket();
        ByteBuf dataBuf = p.GetDatabuff();
        dataBuf.clear();
        dataBuf.writeInt(iMaxSize);
        for (int i = mIndex; i < ListLen; i++) {
            iIndex = i;
            iSize += 1;
            byte[] buf = StringUtil.StringNumto9Byte( _List[iIndex]);
            dataBuf.writeBytes(buf);

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

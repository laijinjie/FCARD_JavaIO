/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Net.PC15.FC8800.Command.Card;

import Net.PC15.Connector.INConnectorEvent;
import Net.PC15.FC8800.Command.Card.Parameter.DeleteCard_Parameter;
import Net.PC15.FC8800.Command.FC8800Command;
import Net.PC15.FC8800.Packet.FC8800PacketCompile;
import Net.PC15.FC8800.Packet.FC8800PacketModel;
import Net.PC15.Util.ByteUtil;
import io.netty.buffer.ByteBuf;
import java.util.ArrayList;

/**
 * 删除卡片
 * @author 赖金杰
 */
public class DeleteCard<T> extends FC8800Command {
    
    protected int mIndex;//指示当前命令进行的步骤
    protected ArrayList<T> _List;
    
    public DeleteCard(){
        
    }
    
    public DeleteCard(DeleteCard_Parameter par) {
        _Parameter = par;
        _List = par.CardList;
        _ProcessMax = _List.size();
        mIndex = 0;
        //初始化缓冲空间
        int iLen = (40 * 5) + 4;
        ByteBuf dataBuf = ByteUtil.ALLOCATOR.buffer(iLen);
        CreatePacket(7, 5, 0, iLen, dataBuf);
        WriteNext();
    }

    /**
     * 写入下一个卡号
     */
    private void WriteNext() {
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
            dataBuf.writeInt((Integer)_List.get(iIndex));
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
    
    @Override
    protected void Release0() {
        _Parameter = null;
        _Result = null;
        _List = null;
    }
    
    @Override
    protected boolean _CommandStep(INConnectorEvent oEvent, FC8800PacketModel model) {
        if (CheckResponseOK(model)) {
            CommandNext(oEvent);
            return true;
        } 
        return false;
    }

    /**
     * 命令继续执行
     */
    protected void CommandNext(INConnectorEvent oEvent) {
        //增加命令进度
        _ProcessStep = mIndex;
        if (mIndex < _List.size()) {
            WriteNext();
        } else {
            RaiseCommandCompleteEvent(oEvent);
        }
        
    }

}

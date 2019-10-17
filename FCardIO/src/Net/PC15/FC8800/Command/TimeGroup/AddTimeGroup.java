/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Net.PC15.FC8800.Command.TimeGroup;

import Net.PC15.Connector.INConnectorEvent;
import Net.PC15.FC8800.Command.Card.Result.WriteCardListBySequence_Result;
import Net.PC15.FC8800.Command.Data.CardDetail;
import Net.PC15.FC8800.Command.Data.TimeGroup.WeekTimeGroup;
import Net.PC15.FC8800.Command.FC8800Command;
import Net.PC15.FC8800.Command.TimeGroup.Parameter.AddTimeGroup_Parameter;
import Net.PC15.FC8800.Packet.FC8800PacketCompile;
import Net.PC15.FC8800.Packet.FC8800PacketModel;
import Net.PC15.Util.ByteUtil;
import io.netty.buffer.ByteBuf;
import java.util.ArrayList;

/**
 * 设置开门时段
 * @author 徐铭康
 */
public class AddTimeGroup extends FC8800Command {

     protected int mIndex;  //指示当前命令进行的步骤
    protected ArrayList<WeekTimeGroup> List;
    
    public AddTimeGroup(AddTimeGroup_Parameter par){
        
        List = par.List;
        _Parameter = par;

        _ProcessMax = List.size();
        mIndex = 0;
        //初始化缓冲空间
        _CreatePacket();
        WriteNext();
    }
    
    protected void _CreatePacket(){
        
        ByteBuf dataBuf = ByteUtil.ALLOCATOR.buffer(225);
        CreatePacket(0x06, 0x03, 0, 225, dataBuf);
    }

    @Override
    protected void Release0() {
        
        
    }

    private void WriteNext() {
        
        
        FC8800PacketCompile compile = (FC8800PacketCompile) _Packet;
        FC8800PacketModel p = (FC8800PacketModel) _Packet.GetPacket();
        ByteBuf dataBuf = p.GetDatabuff();
        dataBuf.clear();
        dataBuf.writeByte(mIndex + 1);
        
        List.get(mIndex).GetBytes(dataBuf);        
        p.SetDataLen(dataBuf.readableBytes());//重置数据长度
        compile.Compile();//重新编译
         mIndex++;
        CommandReady();
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
    protected void CommandNext(INConnectorEvent oEvent){
        //增加命令进度
        _ProcessStep = mIndex;
        if (mIndex < List.size()) {
            WriteNext();
        } else {
            
            
        }
        
    }
}

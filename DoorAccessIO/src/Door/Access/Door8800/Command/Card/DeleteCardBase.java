/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Door.Access.Door8800.Command.Card;

import Door.Access.Connector.INConnectorEvent;
import Door.Access.Door8800.Command.Card.Parameter.DeleteCard_Parameter;
import Door.Access.Door8800.Command.Data.CardDetail;
import Door.Access.Door8800.Command.Door8800Command;
import Door.Access.Door8800.Packet.Door8800PacketCompile;
import Door.Access.Door8800.Packet.Door8800PacketModel;
import Door.Access.Util.ByteUtil;
import io.netty.buffer.ByteBuf;

/**
 *
 * @author 英泽电子
 */
public abstract class DeleteCardBase extends Door8800Command{
    
    protected int mIndex;//指示当前命令进行的步骤
    //protected long [] _List;
    protected CardDetail[] _List;
    public DeleteCardBase(){
        
    }
    

    /**
     * 写入下一个卡号
     */
    protected abstract void WriteNext();
    
    @Override
    protected void Release0() {
        _Parameter = null;
        _Result = null;
        _List = null;
    }
    
    @Override
    protected boolean _CommandStep(INConnectorEvent oEvent, Door8800PacketModel model) {
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
        if (mIndex < _List.length) {
            WriteNext();
        } else {
            RaiseCommandCompleteEvent(oEvent);
        }
        
    }
}

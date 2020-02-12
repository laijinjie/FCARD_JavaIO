/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Door.Access.Door8800.Command.Door;

import Door.Access.Connector.INConnectorEvent;
import Door.Access.Door8800.Command.Door.Parameter.DoorPort_Parameter;
import Door.Access.Door8800.Command.Door.Result.ReadDoorWorkSetting_Result;
import Door.Access.Door8800.Command.Door8800Command;
import Door.Access.Door8800.Packet.Door8800PacketModel;
import Door.Access.Util.ByteUtil;
import io.netty.buffer.ByteBuf;

/**
 * 门的工作模式，可设定多卡、首卡、常开 这三种特殊工作模式。<br/>
 * 成功返回结果参考 {@link ReadDoorWorkSetting_Result}
 *
 * @author 赖金杰
 */
public class ReadDoorWorkSetting extends Door8800Command {
    
    public ReadDoorWorkSetting(DoorPort_Parameter par) {
        _Parameter = par;
        ByteBuf dataBuf = ByteUtil.ALLOCATOR.buffer(1);
        dataBuf.writeByte(par.Door);
        CreatePacket(3, 6, 0, 1, dataBuf);
    }
    
    @Override
    protected void Release0() {
        return;
    }
    
    @Override
    protected boolean _CommandStep(INConnectorEvent oEvent, Door8800PacketModel model) {
        if (CheckResponse_Cmd(model, 3, 6, 0, 0xE5)) {
            ByteBuf buf = model.GetDatabuff();
            
            ReadDoorWorkSetting_Result r = new ReadDoorWorkSetting_Result();
            r.DoorNum = buf.readByte();
            r.Use = buf.readBoolean();
            r.DoorWorkType = buf.readByte();
            r.HoldDoorOption = buf.readByte();
            buf.readByte();
            r.timeGroup.SetBytes(buf);
            //设定返回值
            _Result = r;
            
            RaiseCommandCompleteEvent(oEvent);
            return true;
        } else {
            return false;
        }
    }
    
}

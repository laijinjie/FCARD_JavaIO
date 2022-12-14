/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Door.Access.Door8800.Command.DateTime;

import Door.Access.Command.CommandParameter;
import Door.Access.Connector.INConnectorEvent;
import Door.Access.Door8800.Command.Door8800Command;
import Door.Access.Door8800.Command.DateTime.Result.ReadTime_Result;
import Door.Access.Door8800.Packet.Door8800PacketModel;
import Door.Access.Util.TimeUtil;
import io.netty.buffer.ByteBuf;
import java.util.Calendar;

/**
 * 从控制器中读取控制器时间<br>
 * 成功返回结果参考 {@link ReadTime_Result}
 *
 * @author 赖金杰
 */
public class ReadTime extends Door8800Command {
    
    public ReadTime(CommandParameter par) {
        _Parameter = par;

        CreatePacket(2, 1);
    }
    
    @Override
    protected void Release0() {
        return;
    }
    
    @Override
    protected boolean _CommandStep(INConnectorEvent oEvent, Door8800PacketModel model) {
        if (CheckResponse_Cmd(model, 2, 1, 0, 7)) {
            ByteBuf buf = model.GetDatabuff();
            byte[] datebyte = new byte[7];
            buf.readBytes(datebyte, 0, 7);
            datebyte[5]=datebyte[6];
            Calendar tDate=TimeUtil.BCDTimeToDate_ssmmhhddMMyy(datebyte);
            
            //设定返回值
            _Result = new ReadTime_Result(tDate);
            
            RaiseCommandCompleteEvent(oEvent);
            return true;
        } else {
            return false;
        }
    }

}

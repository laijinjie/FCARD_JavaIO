/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Door.Access.Door8800.Command.Holiday;

import Door.Access.Door8800.Command.Door8800Command;
import Door.Access.Door8800.Command.Holiday.Parameter.DeleteHoliday_Parameter;
import Door.Access.Util.ByteUtil;
import io.netty.buffer.ByteBuf;

/**
 * 删除节假日
 * @author FCARD
 */
public class DeleteHoliday extends Door8800Command {
/**
 * 删除节假日
 * @param parameter 删除节假日参数
 */
    public DeleteHoliday(DeleteHoliday_Parameter parameter){
         _Parameter = parameter;
        ByteBuf buf = parameter.GetBytes();
        CreatePacket(0x04, 0x04, 0x01, buf.readableBytes(), buf);
    }

    @Override
    protected void Release0() {
      
    }
  
   
}

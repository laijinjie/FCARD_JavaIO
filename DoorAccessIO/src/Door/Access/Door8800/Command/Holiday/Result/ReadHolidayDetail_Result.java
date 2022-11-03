/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Door.Access.Door8800.Command.Holiday.Result;

import Door.Access.Command.INCommandResult;
import Door.Access.Door8800.Command.Data.HolidayDBDetail;
import io.netty.buffer.ByteBuf;

/**
 *
 * @author FCARD
 */
public class ReadHolidayDetail_Result implements  INCommandResult{
      public HolidayDBDetail Detail;
      public ReadHolidayDetail_Result(){
          Detail= new HolidayDBDetail();
      }
      
      public  void SetBytes(ByteBuf buf){
          Detail.Capacity=buf.readUnsignedShort();
          Detail.Count=buf.readUnsignedShort();
      }

    @Override
    public void release() {
        
    }
}

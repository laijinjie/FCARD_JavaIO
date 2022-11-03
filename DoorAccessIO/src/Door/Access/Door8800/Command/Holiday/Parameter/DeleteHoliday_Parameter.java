/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Door.Access.Door8800.Command.Holiday.Parameter;

import Door.Access.Command.CommandDetail;
import Door.Access.Command.CommandParameter;
import Door.Access.Door8800.Command.Data.HolidayDetail;
import Door.Access.Util.ByteUtil;
import io.netty.buffer.ByteBuf;
import java.util.ArrayList;

/**
 *删除节假日参数
 * @author FCARD
 */
public class DeleteHoliday_Parameter  extends CommandParameter {

     ArrayList<Integer> IndexList;
     /**
      * 删除节假日参数
      * @param detail 命令参数
      * @param indexList  节假日索引号列表
      */
    public DeleteHoliday_Parameter(CommandDetail detail, ArrayList<Integer> indexList)  {
        super(detail);
        IndexList=indexList;
    }
    public  void checkedParameter() throws Exception{
        if(IndexList==null  ){
            throw new Exception("indexList is null");
        }
        int size=IndexList.size();
        if(size ==0||size>30){
            throw new Exception("indexList Max Size value Range 1-30");
        }
        for (int i = 0; i < 10; i++) {
            int index=IndexList.get(i);
            if(index==0||index>30){
                 throw new Exception(" Index  value Range 1-30");
            }
        }
    }
    public ByteBuf GetBytes() {
        int size = IndexList.size();
        int len = (1 * size) + 4;
        ByteBuf buf = ByteUtil.ALLOCATOR.buffer(len);
        buf.writeInt(size);
        for (int i = 0; i < size; i++) {      
            buf.writeByte(IndexList.get(i));
        }
        return buf;
    }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Face.System.Parameter;

import Door.Access.Command.CommandDetail;
import Door.Access.Command.CommandParameter;
import Door.Access.Util.ByteUtil;
import io.netty.buffer.ByteBuf;

/**
 *
 * @author kaifa
 */
public class WriteOfflineRecordPush_Parameter extends CommandParameter {
    /**
     * 是否开启推送功能
     */
    public  boolean UsePush;



    /**
     * 初始化实例
     * @param use 是否开启推送功能
     */
    public WriteOfflineRecordPush_Parameter(CommandDetail detail,boolean use )
    {
        super(detail);
        UsePush = use;
    }
     /**
     *返回数据包，内部使用
     * @return
     */
    public ByteBuf GetBytes(){
        ByteBuf buf= ByteUtil.ALLOCATOR.buffer(GetDataLen());
        if(UsePush)
            buf.writeByte(1);
        else
            buf.writeByte(0);
        return buf;
    }

    public  int GetDataLen(){
        return 1;
    }
    public  boolean checkedParameter()
    {
        return true;
    }
    
}

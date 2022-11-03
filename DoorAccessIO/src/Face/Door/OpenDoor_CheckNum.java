/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Face.Door;

import Door.Access.Door8800.Command.Door8800Command;
import Door.Access.Util.ByteUtil;
import Face.Door.Parameter.Remote_CheckNum_Parameter;
import io.netty.buffer.ByteBuf;

/**
 *验证码远程开门
 * @author F
 */
public class OpenDoor_CheckNum extends Door8800Command {
/**
 * 验证码远程开门
 * @param par 验证码远程开门参数
 */
    public OpenDoor_CheckNum(Remote_CheckNum_Parameter par){
        _Parameter=par;     
        CreatePacket(0x03, 0x03, 0x80, 0x01, GetBuf(par));
    }
    @Override
    protected void Release0() {      
        return ;
    }
    /**
     * 组装待发送参数
     * @param par
     * @return 待发送数据包
     */
    private ByteBuf GetBuf(Remote_CheckNum_Parameter par){
        ByteBuf buf= ByteUtil.ALLOCATOR.buffer(1);
        buf.writeByte(par.CheckNum);
        return buf;
    }
}

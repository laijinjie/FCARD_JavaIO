/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Door.Access.Door8800.Command.Data;

import Door.Access.Data.AbstractTransaction;
import Door.Access.Data.INData;
import Door.Access.Util.UInt32Util;
import io.netty.buffer.ByteBuf;

/**
 * 监控数据的数据结构
 *
 * @author 赖金杰
 */
public class Door8800WatchTransaction implements INData {

    @Override
    public int GetDataLen() {
        return 0;
    }

    @Override
    public void SetBytes(ByteBuf data) {
        return;
    }

    @Override
    public ByteBuf GetBytes() {
        return null;
    }

    /**
     * 命令类型
     */
    public short CmdType;
    /**
     * 命令索引
     */
    public short CmdIndex;
    /**
     * 命令参数
     */
    public short CmdPar;
    /**
     * 控制器SN
     */
    public String SN;
    /**
     * 记录数据结构
     */
    public AbstractTransaction EventData;
    
    
    @Override
    public String toString() {
        StringBuilder keybuf = new StringBuilder(200);
        keybuf.append("WatchTransaction: Cmd:0x")
                .append(UInt32Util.ToHex(CmdType,2))
                .append(UInt32Util.ToHex(CmdIndex,2))
                .append(UInt32Util.ToHex(CmdPar,2));
         keybuf.append(",SN:").append(SN);
         if(EventData != null)
            keybuf.append(",Detail:").append(EventData.toString());
        
        return keybuf.toString();
    }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Net.PC15.Packet;

import io.netty.buffer.ByteBuf;

/**
 * 所有的命令数据包的接口类
 *
 * @author 赖金杰
 */
public interface INPacket {

    /**
     * 获取数据包的完整数据
     *
     * @return 可用于发送的字节数组
     */
    public ByteBuf GetPacketData();
    
        /**
     * 获取命令实体类
     *
     * @return
     */
    public INPacketModel GetPacket();

    /**
     * 设置命令实体类
     */
    public void SetPacket(INPacketModel packet);
    
    /**
     * 释放命令数据包中的资源，主要是各种 ByteBuf
     */
    public void Release();

}

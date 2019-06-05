/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Net.PC15.Packet;

/**
 *
 * @author 赖金杰
 */
public interface INPacketModel {
    /**
     * 释放命令数据包中的资源，主要是各种 ByteBuf
     */
    public void Release();
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Net.PC15.Packet;

import java.util.ArrayList;
import io.netty.buffer.ByteBuf;

/**
 * 对收到的数据进行命令数据包解析的一系列方法
 *
 * @author 赖金杰
 */
public interface INPacketDecompile {

    /**
     * 对接收到的ByteBuf 进行处理，返回解析结果
     *
     * @param bData 存放待解析数据的缓冲区。
     * @param oRetPack 用来存放解析结果的集合
     * @return True 表示已成功解析了一个数据包，False 表示为成功解析数据包
     */
    public boolean Decompile(ByteBuf bData, ArrayList<INPacketModel> oRetPack);

    /**
     * 清空上次解析后缓存的内容。
     */
    public void ClearBuf();

    /**
     * 释放命令数据包中的资源，主要是各种 ByteBuf
     */
    public void Release();
}

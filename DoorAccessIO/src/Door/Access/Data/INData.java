/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Door.Access.Data;
import io.netty.buffer.ByteBuf;

/**
 * 所有数据的抽象接口
 * @author 赖金杰
 */
public interface INData {
    
    /**
     * 获取数据的字节长度
     * @return 返回数据的字节长度
     */
    public int GetDataLen();
    
    /**
     * 将一个缓冲区 ByteBuf 设置到数据结构中
     * @param data 需要设置到结构中的ByteBuf 
     */
    public void SetBytes(ByteBuf data);
    
    
    /**
     * 获取一个 ByteBuf 此 缓冲中包含了此数据结构的所有数据
     * @return 返回一个包含此结构的ByteBuf；
     */
    public ByteBuf GetBytes();
            
    
}

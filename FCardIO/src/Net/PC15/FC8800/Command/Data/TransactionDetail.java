/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Net.PC15.FC8800.Command.Data;

import Net.PC15.Data.INData;
import io.netty.buffer.ByteBuf;

/**
 * 事件日志详情，包含数据库容量，记录索引，已读取索引，循环标志4个部分
 *
 * @author 赖金杰
 */
public class TransactionDetail implements INData {

    /**
     * 数据库容量
     */
    public long DataBaseMaxSize;
    /**
     * 写索引号(记录尾号)
     */
    public long WriteIndex;
    /**
     * 读索引号(上传断点)
     */
    public long ReadIndex;
    /**
     * 循环标记
     */
    public boolean IsCircle;

    @Override
    public int GetDataLen() {
        return 0x0D;
    }

    @Override
    public void SetBytes(ByteBuf data) {
        DataBaseMaxSize = data.readUnsignedInt();
        WriteIndex = data.readUnsignedInt();
        ReadIndex = data.readUnsignedInt();
        IsCircle = data.readBoolean();
        return;
    }

    @Override
    public ByteBuf GetBytes() {
        return null;
    }

    /**
     * 可用的新记录数。
     *
     * @return 新记录数
     */
    public long readable() {
        if (IsCircle) {
            return DataBaseMaxSize;
        }
        if (WriteIndex > DataBaseMaxSize) {
            WriteIndex = 0;
        }
        if (ReadIndex > DataBaseMaxSize) {
            ReadIndex = 0;
        }
        if (ReadIndex == WriteIndex) {
            return 0;
        }
        //记录尾号大于上传断点，那么表示新记录只有上传断点至记录尾号之间这段。
        if (WriteIndex > ReadIndex) {
            return (WriteIndex - ReadIndex);
        }
        //记录尾号小于上传断点，那么表示新记录有两段，一段是上传断点至记录末，一处是记录头至记录尾号
        if (WriteIndex < ReadIndex) {
            return WriteIndex + (DataBaseMaxSize - ReadIndex);
        }
        return 0;
    }
    
    /**
     * 可用的新记录数。
     *
     * @return 新记录数
     */
    public long NewSzie()
    {
        return readable();
    }

}

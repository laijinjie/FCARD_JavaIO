/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Net.PC15.FC8800.Command.Data;

import Net.PC15.Data.AbstractTransaction;
import Net.PC15.Util.TimeUtil;
import io.netty.buffer.ByteBuf;
import java.util.Calendar;

/**
 * 自定义的事件
 *
 * @author 赖金杰
 */
public class DefinedTransaction extends AbstractTransaction {

    /**
     * 事件附带的数据长度
     */
    public int DataLen;

    /**
     * 事件附带的数据内容
     */
    public byte[] DataByteBuf;

    /**
     * 自定义的事件
     *
     * @param iType 事件类型
     * @param iCode 事件代码
     * @param tDate 发生事件
     */
    public DefinedTransaction(int iType, int iCode, Calendar tDate) {
        _TransactionType = (short) iType;
        _TransactionCode = (short) iCode;
        _TransactionDate = (Calendar) tDate.clone();
        DataLen = 0;
    }

    public void SetWatchData(ByteBuf data) {
        if (data == null) {
            DataLen = 0;
            return;
        }
        DataLen = data.readableBytes();
        if (DataLen > 0) {
            DataByteBuf = new byte[DataLen];
            data.readBytes(DataByteBuf);
        }

    }

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

}

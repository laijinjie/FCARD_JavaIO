/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Door.Access.Door8800.Command.Data;

import Door.Access.Data.AbstractTransaction;
import Door.Access.Util.TimeUtil;
import Door.Access.Util.UInt32Util;
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

    @Override
    public String toString() {
        StringBuilder keybuf = new StringBuilder(200);

        keybuf.append("TransactionDate:")
                .append(TimeUtil.FormatTime(_TransactionDate))
                .append(",TransactionCode:0x")
                .append(UInt32Util.ToHex(_TransactionCode,2))
                .append(",TransactionType:0x")
                .append(UInt32Util.ToHex(_TransactionType,2));
        return keybuf.toString();
    }
}

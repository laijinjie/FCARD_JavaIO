/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Door.Access.Data;

import Door.Access.Util.TimeUtil;
import io.netty.buffer.ByteBuf;
import java.util.Calendar;

/**
 * 保活包消息
 * @author kaifa
 */
public class KeepAliveTransaction extends AbstractTransaction {

    /**
     * 保活包消息
     */
    public KeepAliveTransaction() {
        _TransactionType = 0x22;
        SetBytes(null);
    }

    @Override
    public int GetDataLen() {
        return 0;
    }

    @Override
    public void SetBytes(ByteBuf data) {
        _IsNull = false;

        _TransactionCode = 1;
        _TransactionDate = Calendar.getInstance();
    }

    @Override
    public ByteBuf GetBytes() {
        return null;
    }

    @Override
    public String toString() {
        StringBuilder keybuf = new StringBuilder(200);
        keybuf.append("KeepAliveTransaction -- ")
                .append("TransactionDate:")
                .append(TimeUtil.FormatTime(_TransactionDate));
        return keybuf.toString();
    }
}

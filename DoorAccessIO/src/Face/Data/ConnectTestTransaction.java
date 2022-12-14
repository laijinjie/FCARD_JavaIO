package Face.Data;

import Door.Access.Data.AbstractTransaction;
import Door.Access.Util.TimeUtil;
import io.netty.buffer.ByteBuf;

import java.util.Calendar;

/**
 * 连接测试消息
 */
public class ConnectTestTransaction extends AbstractTransaction {
    /**
     * 连接测试消息
     */
    public ConnectTestTransaction() {
        _TransactionType = 0xA0;
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
        keybuf.append("ConnectTestTransaction -- ")
                .append("TransactionDate:")
                .append(TimeUtil.FormatTime(_TransactionDate));
        return keybuf.toString();
    }
}

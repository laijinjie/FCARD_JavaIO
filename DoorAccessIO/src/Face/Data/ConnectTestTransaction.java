package Face.Data;

import Door.Access.Data.AbstractTransaction;
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
}

package Face.Transaction.Parameter;

import Door.Access.Command.CommandDetail;
import Door.Access.Command.CommandParameter;
import Door.Access.Util.ByteUtil;
import Face.Data.e_TransactionDatabaseType;
import io.netty.buffer.ByteBuf;

/**
 * 清空指定类型的记录数据库
 * @author F
 */
public class ClearTransactionDatabase_Parameter extends CommandParameter {
    /**
     * 记录类型
     */
    public e_TransactionDatabaseType DatabaseType;

    /**
     * 清空指定类型的记录数据库
     * @param detail 通讯参数
     * @param databaseType 记录类型
     */
    public ClearTransactionDatabase_Parameter(CommandDetail detail, e_TransactionDatabaseType databaseType) {
        super(detail);
        DatabaseType = databaseType;
    }
  /**
     * 数据包转换
     * @return 
     */
    public ByteBuf getBytes() {
        ByteBuf buf = ByteUtil.ALLOCATOR.buffer(getDataLen());
        buf.writeByte(DatabaseType.getValue());
        return buf;
    }
  /**
     * 数据包长度
     * @return 
     */
    public int getDataLen() {
        return 1;
    }
}

package Face.Transaction.Parameter;

import Door.Access.Command.CommandDetail;
import Door.Access.Command.CommandParameter;
import Door.Access.Util.ByteUtil;
import Face.Data.e_TransactionDatabaseType;
import io.netty.buffer.ByteBuf;

/**
 * 指定记录数据库的读索引
 * @author F
 */
public class WriteTransactionDatabaseWriteIndex_Parameter extends CommandParameter {

    /**
     * 记录数据库类型<br>
     * <ul>
     * <li>1 &emsp; 读卡记录    </li>
     * <li>2 &emsp; 门磁相关记录</li>
     * <li>3 &emsp; 系统相关记录    </li>
     * <li>4 &emsp; 体温记录</li>
     * </ul>
     */
    public Face.Data.e_TransactionDatabaseType DatabaseType;
    /**
     * 数据库中的写索引号<br>
     * 记录尾地址
     */
    public int WriteIndex;
    /**
     * 指定记录数据库的读索引
     * @param detail 通讯命令
     * @param _DatabaseType 记录类型
     */
    public WriteTransactionDatabaseWriteIndex_Parameter(CommandDetail detail, e_TransactionDatabaseType _DatabaseType) {
        super(detail);
        DatabaseType=_DatabaseType;
    }
    /**
     * 数据转换（内部方法）
     * @return 
     */
    public ByteBuf getBytes(){
        ByteBuf buf= ByteUtil.ALLOCATOR.buffer(getDataLen());
        buf.writeByte(DatabaseType.getValue());
        buf.writeInt(WriteIndex);
        return buf;
    }
    /**
     * 发送的数据包长度
     * @return 
     */
    public  int getDataLen(){
        return 5;
    }
}

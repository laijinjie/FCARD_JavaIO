package Face.Transaction.Parameter;

import Door.Access.Command.CommandDetail;
import Door.Access.Command.CommandParameter;
import Door.Access.Util.ByteUtil;
import Face.Data.e_TransactionDatabaseType;
import io.netty.buffer.ByteBuf;

/**
 * 修改指定记录数据库的读索引
 * @author F
 */
public class WriteTransactionDatabaseReadIndex_Parameter extends CommandParameter {

    /**
     * 记录数据库类型<br>
     * <ul>
     * <li>1 &emsp; 读卡记录    </li>
     * <li>2 &emsp; 门磁相关记录</li>
     * <li>3 &emsp; 系统相关记录    </li>
     * <li>4 &emsp; 体温记录</li>
     * </ul>
     */
    public e_TransactionDatabaseType DatabaseType;
    /**
     * 数据库中的读索引号
     */
    public int ReadIndex;

    /**
     * 修改指定记录数据库的读索引
     *
     * @param detail 包含命令的执行时的一些必要信息，命令执行的连接器通道，命令身份验证信息，用户附加数据，超时重试参数
     * @param type   取值范围 1-6
     */
    public WriteTransactionDatabaseReadIndex_Parameter(CommandDetail detail, e_TransactionDatabaseType type) {
        super(detail);
        DatabaseType =type;
    }
    /**
     * 数据包转换
     * @return 
     */
    public ByteBuf getBytes(){
        ByteBuf dataBuf = ByteUtil.ALLOCATOR.buffer(getDataLen());
        dataBuf.writeByte(DatabaseType.getValue());
        dataBuf.writeInt(ReadIndex);
        return dataBuf;
    }
    /**
     * 数据包长度
     * @return 
     */
    public int getDataLen(){
        return 5;
    }
}

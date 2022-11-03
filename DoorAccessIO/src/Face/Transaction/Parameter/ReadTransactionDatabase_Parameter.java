package Face.Transaction.Parameter;

import Door.Access.Command.CommandDetail;
import Door.Access.Command.CommandParameter;
import Face.Data.e_TransactionDatabaseType;

/**
 * 读取新记录
 * @author F
 */
public class ReadTransactionDatabase_Parameter extends CommandParameter {
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
     * 读取数量 0-160000,0表示都取所有新记录
     */
    public int Quantity;
    /**
     * 每次读取数量 1-300
     */
    public int PacketSize;

    /**
     * 读取新记录
     * 每次最大读取60条
     * @param detail 包含命令的执行时的一些必要信息，命令执行的连接器通道，命令身份验证信息，用户附加数据，超时重试参数
     * @param type 取值范围 1-5
     */
    public ReadTransactionDatabase_Parameter(CommandDetail detail, e_TransactionDatabaseType type) {
        super(detail);
        DatabaseType = type;
        PacketSize = 60;
        Quantity = 0;
    }
}

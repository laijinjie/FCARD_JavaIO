package Face.Transaction.Result;

import Door.Access.Command.INCommandResult;
import Door.Access.Data.AbstractTransaction;
import Face.Data.e_TransactionDatabaseType;

import java.util.ArrayList;

/**
 * 读取控制器中的卡片数据库信息返回值
 * @author F
 */
public class ReadTransactionDatabaseByIndex_Result implements INCommandResult {
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
     * 读索引号
     */
    public int ReadIndex;

    /**
     * 读取数量
     */
    public int Quantity;

    /**
     * 记录列表
     */
    public ArrayList<AbstractTransaction> TransactionList;

    @Override
    public void release() {

    }
}

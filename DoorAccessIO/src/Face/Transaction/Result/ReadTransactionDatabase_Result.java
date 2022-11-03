package Face.Transaction.Result;

import Door.Access.Command.INCommandResult;
import Door.Access.Data.AbstractTransaction;
import Face.Data.e_TransactionDatabaseType;

import java.util.ArrayList;

/**
 * 读取记录存储详情返回值
 *
 * @author F
 */
public class ReadTransactionDatabase_Result implements INCommandResult {

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
     * 读取数量
     */
    public int Quantity;

    /**
     * 剩余新记录数量
     */
    public int readable;

    /**
     * 记录列表
     */
    public ArrayList<AbstractTransaction> TransactionList;

    /**
     * 读取记录存储详情返回值
     */
    public ReadTransactionDatabase_Result() {
        Quantity = 0;
        readable = 0;
    }

    @Override
    public void release() {

    }
}

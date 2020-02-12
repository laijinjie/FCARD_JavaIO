/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Door.Access.Door8800.Command.Transaction.Result;

import Door.Access.Command.INCommandResult;
import Door.Access.Data.AbstractTransaction;
import Door.Access.Door8800.Command.Data.TransactionDatabaseDetail;
import Door.Access.Door8800.Command.Transaction.e_TransactionDatabaseType;
import java.util.ArrayList;

/**
 * 读取控制器中的卡片数据库信息返回值
 *
 * @author 赖金杰
 */
public class ReadTransactionDatabaseByIndex_Result  implements INCommandResult {

        /**
     * 记录数据库类型<br/>
     * <ul>
     * <li>1 &emsp; 读卡记录    </li>
     * <li>2 &emsp; 出门开关记录</li>
     * <li>3 &emsp; 门磁记录    </li>
     * <li>4 &emsp; 软件操作记录</li>
     * <li>5 &emsp; 报警记录    </li>
     * <li>6 &emsp; 系统记录    </li>
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
    

    public ReadTransactionDatabaseByIndex_Result() {
        
    }

    @Override
    public void release() {

    }

}

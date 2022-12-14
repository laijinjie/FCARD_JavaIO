/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Door.Access.Door8800.Command.Transaction.Parameter;

import Door.Access.Command.CommandDetail;
import Door.Access.Command.CommandParameter;
import Door.Access.Door8800.Command.Transaction.e_TransactionDatabaseType;

/**
 * 按索引号读取记录数据库
 *
 * @author 赖金杰
 */
public class ReadTransactionDatabaseByIndex_Parameter extends CommandParameter {

    /**
     * 记录数据库类型<br>
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
     * 读取数量 1-400
     */
    public int Quantity;

    /**
     * 按索引号读取记录数据库
     *
     * @param detail 包含命令的执行时的一些必要信息，命令执行的连接器通道，命令身份验证信息，用户附加数据，超时重试参数
     * @param type 取值范围 1-6
     */
    public ReadTransactionDatabaseByIndex_Parameter(CommandDetail detail, e_TransactionDatabaseType type) {
        super(detail);
        DatabaseType = type;
    }

}

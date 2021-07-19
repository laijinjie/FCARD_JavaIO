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
 * 读取新记录
 *
 * @author 赖金杰
 */
public class ReadTransactionDatabase_Parameter extends CommandParameter {

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
     * 读取数量 0-160000,0表示都取所有新记录
     */
    public int Quantity;

    /**
     * 每次读取数量 1-300
     */
    public int PacketSize;

    /**
     * 按索引号读取记录数据库
     *
     * @param detail 包含命令的执行时的一些必要信息，命令执行的连接器通道，命令身份验证信息，用户附加数据，超时重试参数
     * @param type 取值范围 1-6
     */
    public ReadTransactionDatabase_Parameter(CommandDetail detail, e_TransactionDatabaseType type) {
        super(detail);
        DatabaseType = type;
        PacketSize = 200;
        Quantity = 0;
    }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Door.Access.Door8800.Command.Transaction.Result;

import Door.Access.Command.INCommandResult;
import Door.Access.Door8800.Command.Data.TransactionDatabaseDetail;

/**
 * 读取记录数据库详情的返回值
 *
 * @author 赖金杰
 */
public class ReadTransactionDatabaseDetail_Result implements INCommandResult {

    /**
     * 记录数据库的详情
     */
    public TransactionDatabaseDetail DatabaseDetail;

    public ReadTransactionDatabaseDetail_Result() {
        DatabaseDetail = new TransactionDatabaseDetail();
    }

    @Override
    public void release() {

    }

}

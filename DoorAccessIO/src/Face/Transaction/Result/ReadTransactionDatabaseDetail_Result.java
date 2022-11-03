package Face.Transaction.Result;

import Door.Access.Command.INCommandResult;
import Face.Data.TransactionDatabaseDetail;
import io.netty.buffer.ByteBuf;

/**
 * 读取控制器中的卡片数据库信息
 * @author F
 */
public class ReadTransactionDatabaseDetail_Result implements INCommandResult {
    /**
     *记录数据库的详情
     */
    public TransactionDatabaseDetail DatabaseDetail;
    /**
     * 读取控制器中的卡片数据库信息
     */
    public ReadTransactionDatabaseDetail_Result()
    {
        DatabaseDetail = new TransactionDatabaseDetail();
    }
    /**
     * 数据转换
     * @param buf 
     */
    public void setBytes(ByteBuf buf){
        DatabaseDetail.SetBytes(buf);
    }
    @Override
    public void release() {

    }
}

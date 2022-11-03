package Face.Transaction;

import Door.Access.Door8800.Command.Door8800Command;
import Face.Transaction.Parameter.WriteTransactionDatabaseWriteIndex_Parameter;

/**
 * 修改指定记录数据库的写索引-记录尾号
 *
 * @author F
 */
public class WriteTransactionDatabaseWriteIndex extends Door8800Command {
    /**
     * 修改指定记录数据库的写索引-记录尾号
     * @param parameter 记录尾号参数
     */
    public WriteTransactionDatabaseWriteIndex(WriteTransactionDatabaseWriteIndex_Parameter parameter) {
        _Parameter = parameter;
        CreatePacket(0x08, 0x03, 0x01, parameter.getDataLen(), parameter.getBytes());
    }

    @Override
    protected void Release0() {

    }
}

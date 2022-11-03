package Face.Transaction;

import Door.Access.Door8800.Command.Door8800Command;
import Face.Transaction.Parameter.WriteTransactionDatabaseReadIndex_Parameter;

/**
 * 更新记录指针
 * @author F
 */
public class WriteTransactionDatabaseReadIndex extends Door8800Command {
    /**
     * 更新记录指针
     * @param parameter 更新记录指针参数
     */
    public WriteTransactionDatabaseReadIndex(WriteTransactionDatabaseReadIndex_Parameter parameter) {
        _Parameter = parameter;
        CreatePacket(0x08, 0x03, 0x00, parameter.getDataLen(), parameter.getBytes());
    }

    @Override
    protected void Release0() {

    }
}

package Face.Transaction;

import Door.Access.Door8800.Command.Door8800Command;
import Face.Transaction.Parameter.ClearTransactionDatabase_Parameter;

/**
 * 清空指定类型的记录数据库
 * @author F
 */
public class ClearTransactionDatabase extends Door8800Command {
    /**
     * 清空指定类型的记录数据库
     * @param parameter 清空指定类型的记录数据库参数
     */
    public  ClearTransactionDatabase(ClearTransactionDatabase_Parameter parameter)
    {
        _Parameter=parameter;
        CreatePacket(0x08, 0x02, 0x01, parameter.getDataLen(),parameter.getBytes());
    }
    @Override
    protected void Release0() {

    }
}

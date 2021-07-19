/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Door.Access.Door8800.Command.Transaction;

import Door.Access.Door8800.Command.Door8800Command;
import Door.Access.Door8800.Command.Transaction.Parameter.WriteTransactionDatabaseWriteIndex_Parameter;
import Door.Access.Util.ByteUtil;
import io.netty.buffer.ByteBuf;

/**
 * 修改指定记录数据库的写索引<br>
 * 记录尾地址
 *
 * @author 赖金杰
 */
public class WriteTransactionDatabaseWriteIndex extends Door8800Command {

    public WriteTransactionDatabaseWriteIndex(WriteTransactionDatabaseWriteIndex_Parameter par) {
        _Parameter = par;
        ByteBuf dataBuf = ByteUtil.ALLOCATOR.buffer(5);
        dataBuf.writeByte(par.DatabaseType.getValue());
        dataBuf.writeInt(par.WriteIndex);
        CreatePacket(8, 3, 1, 5, dataBuf);
    }

    @Override
    protected void Release0() {
        return;
    }

}

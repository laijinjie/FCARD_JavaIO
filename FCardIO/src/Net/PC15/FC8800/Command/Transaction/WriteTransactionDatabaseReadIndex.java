/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Net.PC15.FC8800.Command.Transaction;

import Net.PC15.FC8800.Command.FC8800Command;
import Net.PC15.FC8800.Command.Transaction.Parameter.WriteTransactionDatabaseReadIndex_Parameter;
import Net.PC15.Util.ByteUtil;
import io.netty.buffer.ByteBuf;

/**
 * 修改指定记录数据库的读索引<br/>
 * 更新记录指针
 * @author 赖金杰
 */
public class WriteTransactionDatabaseReadIndex extends FC8800Command {

    public WriteTransactionDatabaseReadIndex(WriteTransactionDatabaseReadIndex_Parameter par) {
        _Parameter = par;
        ByteBuf dataBuf = ByteUtil.ALLOCATOR.buffer(6);
        dataBuf.writeByte(par.DatabaseType.getValue());
        dataBuf.writeInt(par.ReadIndex);
        dataBuf.writeBoolean(par.IsCircle);
        CreatePacket(8, 3, 0, 6, dataBuf);
    }

    @Override
    protected void Release0() {
        return;
    }
    
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Door.Access.Door8800.Command.Transaction;

import Door.Access.Door8800.Command.Transaction.Parameter.ClearTransactionDatabase_Parameter;
import Door.Access.Door8800.Command.Door8800Command;
import Door.Access.Util.ByteUtil;
import io.netty.buffer.ByteBuf;

/**
 * 清空指定类型的记录数据库
 * @author 赖金杰
 */
public class ClearTransactionDatabase extends Door8800Command {

    public ClearTransactionDatabase(ClearTransactionDatabase_Parameter par) {
        _Parameter = par;
        ByteBuf dataBuf = ByteUtil.ALLOCATOR.buffer(1);
        dataBuf.writeByte(par.DatabaseType.getValue());
        CreatePacket(8, 2, 1, 1, dataBuf);
    }

    @Override
    protected void Release0() {
        return;
    }
    
}

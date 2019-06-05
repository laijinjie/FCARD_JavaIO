/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Net.PC15.FC8800.Command.Transaction;

import Net.PC15.FC8800.Command.Transaction.Parameter.ClearTransactionDatabase_Parameter;
import Net.PC15.FC8800.Command.FC8800Command;
import Net.PC15.Util.ByteUtil;
import io.netty.buffer.ByteBuf;

/**
 * 清空指定类型的记录数据库
 * @author 赖金杰
 */
public class ClearTransactionDatabase extends FC8800Command {

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

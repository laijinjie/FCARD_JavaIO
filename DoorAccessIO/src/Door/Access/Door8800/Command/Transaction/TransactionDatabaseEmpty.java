/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Door.Access.Door8800.Command.Transaction;

import Door.Access.Command.CommandParameter;
import Door.Access.Door8800.Command.Door8800Command;

/**
 * 清空所有类型的记录数据库
 * @author 赖金杰
 */
public class TransactionDatabaseEmpty extends Door8800Command {

    public TransactionDatabaseEmpty(CommandParameter par) {
        _Parameter = par;
        CreatePacket(8, 2);
    }

    @Override
    protected void Release0() {
        return;
    }
    
}

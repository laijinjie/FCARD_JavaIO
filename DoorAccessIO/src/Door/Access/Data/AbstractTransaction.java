/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Door.Access.Data;

import java.util.Calendar;

/**
 * 所有事务、记录的抽象类
 *
 * @author 赖金杰
 */
public abstract class AbstractTransaction implements INData {

    public int SerialNumber; //事务序列号
    protected Calendar _TransactionDate; //事务时间
    protected short _TransactionType; //事务类型
    protected short _TransactionCode; //事务代码
    /**
     * 记录是否为空记录
     */
    protected boolean _IsNull;

    /**
     * 事务时间
     *
     * @return 事务时间
     */
    public Calendar TransactionDate() {
        return _TransactionDate;
    }

    /**
     * 记录是否为空记录
     */
    public boolean IsNull() {
        return _IsNull;
    }

    /**
     * 事务类型
     *
     * @return 事务类型
     */
    public short TransactionType() {
        return _TransactionType;
    }

    /**
     * 事务代码
     *
     * @return 事务代码
     */
    public short TransactionCode() {
        return _TransactionCode;
    }
}

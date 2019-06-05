/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Net.PC15.FC8800.Command.Transaction;

/**
 * 记录数据库类型
 * @author 赖金杰
 */
public enum e_TransactionDatabaseType {
    /**
     * 读卡相关记录
     */
    OnCardTransaction(1),
    /**
     * 出门开关相关记录
     */
    OnButtonTransaction(2),
    /**
     * 门磁相关记录
     */
    OnDoorSensorTransaction(3),
    /**
     * 远程操作相关记录
     */
    OnSoftwareTransaction(4),
    /**
     * 报警相关记录
     */
    OnAlarmTransaction(5),
    /**
     * 系统相关记录
     */
    OnSystemTransaction(6);

    private final int value;

    //构造器默认也只能是private, 从而保证构造函数只能在内部使用

    e_TransactionDatabaseType(int value) {
        this.value = value;
    }
    
    public static e_TransactionDatabaseType valueOf(int iValue)
    {
        e_TransactionDatabaseType[] lst = values();
        for (e_TransactionDatabaseType t : lst) {
            if(t.getValue() ==iValue)
            {
                return t;
            }
        }
        return null;
    }
    /**
     * 获取枚举值
     * @return 类型代码
     */
    public int getValue() {
        return value;
    }
}

package Face.Data;
/**
 * 记录类型
 * @author F
 */
public enum  e_TransactionDatabaseType {
    /**
     * 读卡相关记录
     */
    OnCardTransaction (1),
    /**
     * 门磁相关记录
     */
    OnDoorSensorTransaction(2),
    /**
     * 系统相关记录
     */
    OnSystemTransaction (3),
    /**
     * 体温记录
     */
    OnBodyTemperatureTransaction (4);

    private final int value;

    //构造器默认也只能是private, 从而保证构造函数只能在内部使用

    e_TransactionDatabaseType(int value) {
        this.value = value;
    }
/**
 * 值类型转枚举类型
 * @param iValue
 * @return 
 */
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

package Face.Data;

import Door.Access.Util.TimeUtil;

/**
 *门磁记录<br>
 * TransactionCode 事件代码含义表：<br>
 * 1  开门<br>
 * 2  关门 <br>
 * 3  进入门磁报警状态<br>
 * 4  退出门磁报警状态<br>
 * 5  门未关好<br>
 * 6  使用按钮开门<br>
 * 7  按钮开门时门已锁定<br>
 * 8  按钮开门时控制器已过期<br>
 */
public class DoorSensorTransaction extends SystemTransaction {
    /**
     * 门磁记录
     */
    public DoorSensorTransaction()
    {
        _TransactionType = 2;
    }
}

package Face.Data;

import Door.Access.Data.AbstractTransaction;
import Door.Access.Util.TimeUtil;
import io.netty.buffer.ByteBuf;

/**
 * 系统记录<br>
 * 1   软件开门<br>
 * 2   软件关门<br>
 * 3   软件常开<br>
 * 4   控制器自动进入常开<br>
 * 5   控制器自动关闭门<br>
 * 6   长按出门按钮常开<br>
 * 7   长按出门按钮常闭<br>
 * 8   软件锁定<br>
 * 9   软件解除锁定<br>
 * 10  控制器定时锁定--到时间自动锁定<br>
 * 11  控制器定时锁定--到时间自动解除锁定<br>
 * 12  报警--锁定<br>
 * 13  报警--解除锁定<br>
 * 14  非法认证报警<br>
 * 15  门磁报警<br>
 * 16  胁迫报警<br>
 * 17  开门超时报警<br>
 * 18  黑名单报警<br>
 * 19  消防报警<br>
 * 20  防拆报警<br>
 * 21  非法认证报警解除<br>
 * 22  门磁报警解除<br>
 * 23  胁迫报警解除<br>
 * 24 开门超时报警解除<br>
 * 25 黑名单报警解除<br>
 * 26 消防报警解除<br>
 * 27 防拆报警解除<br>
 * 28 系统加电<br>
 * 29 系统错误复位（看门狗）<br>
 * 30 设备格式化记录<br>
 * 31 读卡器接反<br>
 * 32 读卡器线路未接好<br>
 * 33 无法识别的读卡器<br>
 * 34 网线已断开<br>
 * 35 网线已插入<br>
 * 36 WIFI 已连接<br>
 * 37 WIFI 已断开<br>
 */
public class SystemTransaction extends AbstractTransaction {

     int Door;
    /**
     * 门号
     * @return 门号
     */
     public int getDoor(){
         return Door;
     }
     public SystemTransaction(){
         _TransactionType = 3;
         _IsNull = false;
     }
    @Override
    public int GetDataLen() {
        return 8;
    }

    @Override
    public void SetBytes(ByteBuf data) {
            if(IsNull()){
                data.clear();
                return;
            }
        Door = data.readByte();
        byte[] time = new byte[6];
        data.readBytes(time, 0, 6);
        _TransactionDate = TimeUtil.BCDTimeToDate_ssmmhhddMMyy(time);
        _TransactionCode = data.readByte();
    }

    @Override
    public ByteBuf GetBytes() {
        return null;
    }
}

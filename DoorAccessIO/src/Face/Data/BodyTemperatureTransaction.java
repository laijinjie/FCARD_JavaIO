package Face.Data;

import Door.Access.Data.AbstractTransaction;
import io.netty.buffer.ByteBuf;

import java.util.Calendar;

/**
 * 体温记录
 */
public class BodyTemperatureTransaction extends AbstractTransaction {

    public  int _RecordSerialNumber;
    public int _Temperature;

    /**
     * 序列号
     * @return
     */
    public  int getRecordSerialNumber(){
        return _RecordSerialNumber;
    }

    /**
     * 获取体温（需要在原有数值上除以10得到的值就是具体的温度值）
     * @return
     */
    public  int getBodyTemperature(){
        return _Temperature;
    }
    @Override
    public int GetDataLen() {
        return 6;
    }

    @Override
    public void SetBytes(ByteBuf data) {
        if(IsNull()){
            data.clear();
            return;
        }
        _RecordSerialNumber=SerialNumber;
        SerialNumber=data.readInt();
        _TransactionDate= Calendar.getInstance();
        _TransactionCode=1;
        _Temperature=data.readUnsignedShort();
    }

    @Override
    public ByteBuf GetBytes() {
        return null;
    }
}

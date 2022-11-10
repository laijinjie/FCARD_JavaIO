package Face.Data;

import Door.Access.Data.AbstractTransaction;
import Door.Access.Util.TimeUtil;
import io.netty.buffer.ByteBuf;

/**
 * 认证记录
 */
public class CardTransaction extends AbstractTransaction {

    long RecordSerialNumber;
    long UserCode;
    byte Photo;
    byte AccessType;

    /**
     * 记录唯一序号
     *
     * @return 序号
     */
    public long getRecordSerialNumber() {
        return RecordSerialNumber;
    }

    /**
     * 用户号
     *
     * @return 用户号
     */
    public long getUserCode() {
        return UserCode;
    }

    /**
     * 是否包含照片
     *
     * @return
     */
    public byte getPhoto() {
        return Photo;
    }

    /**
     * 出入类型
     *
     * @return 1--表示进门；2--表示出门
     */
    public byte getAccessType() {
        return AccessType;
    }

    @Override
    public int GetDataLen() {
        return 13;
    }

    @Override
    public void SetBytes(ByteBuf data) {
        RecordSerialNumber = data.readUnsignedInt();
        SerialNumber = (int) RecordSerialNumber;
        UserCode = data.readUnsignedInt();
        byte[] time = new byte[6];
        data.readBytes(time, 0, 6);
        _TransactionDate = TimeUtil.BCDTimeToDate_ssmmhhddMMyy(time);
        AccessType = data.readByte();
        _TransactionCode = data.readByte();
        Photo = data.readByte();
    }

    @Override
    public ByteBuf GetBytes() {
        return null;
    }

    /**
     * 认证记录<br>
     *  TransactionCode 事件代码含义表：<br>
     * 1	刷卡验证<br>
     * 2	指纹验证<br>
     * 3	人脸验证<br>
     * 4	指纹 + 刷卡<br>
     * 5	人脸 + 指纹<br>
     * 6	人脸 + 刷卡<br>
     * 7	刷卡 + 密码<br>
     * 8	人脸 + 密码<br>
     * 9	指纹 + 密码<br>
     * 10	手动输入用户号加密码验证<br>
     * 11	指纹+刷卡+密码<br>
     * 12	人脸+刷卡+密码<br>
     * 13	人脸+指纹+密码<br>
     * 14	人脸+指纹+刷卡<br>
     * 15	重复验证<br>
     * 16	有效期过期<br>
     * 17	开门时段过期<br>
     * 18	节假日时不能开门<br>
     * 19	未注册用户<br>
     * 20	探测锁定<br>
     * 21	有效次数已用尽<br>
     * 22	锁定时验证，禁止开门<br>
     * 23	挂失卡<br>
     * 24	黑名单卡<br>
     * 25	免验证开门 -- 按指纹时用户号为0，刷卡时用户号是卡号<br>
     * 26	禁止刷卡验证  --  【权限认证方式】中禁用刷卡时<br>
     * 27	禁止指纹验证  --  【权限认证方式】中禁用指纹时<br>
     * 28	控制器已过期<br>
     * 29	验证通过—有效期即将过期<br>
     */
    public CardTransaction() {
        _TransactionType = 1;
    }
    
    @Override
    public String toString() {
        StringBuilder keybuf = new StringBuilder(200);
        keybuf.append("RecordSerialNumber:")
                .append(RecordSerialNumber)
                .append(",UserCode:")
                .append(UserCode)
                .append(",TransactionDate:")
                .append(TimeUtil.FormatTime(_TransactionDate))
                .append(",AccessType:")
                .append(AccessType)
                .append(",TransactionCode:")
                .append(_TransactionCode)
                .append(",Photo:")
                .append(Photo);
        return keybuf.toString();
    }
}

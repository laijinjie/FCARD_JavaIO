package Face.Data;

import Door.Access.Util.ByteUtil;
import Door.Access.Util.StringUtil;
import Door.Access.Util.TimeUtil;
import io.netty.buffer.ByteBuf;

import java.io.UnsupportedEncodingException;
import java.util.Calendar;

/**
 * 人员信息
 * @author F
 */
public class Person implements Comparable<Person> {
    /**
     *用户号
     */
    public int UserCode;
    /**
     *卡号，取值范围 0x1-0xFFFFFFFF
     */
    public long CardData;
    /**
     *卡密码,无密码不填。密码是4-8位的数字。
     */
    public String Password;
    /**
     *截止日期，最大2089年12月31日
     */
    public Calendar Expiry;
    /**
     *开门时段 取值范围：1-64；
     */
    public int TimeGroup;
    /**
     *有效次数,取值范围：0-65535,0表示次数用光了。65535表示不受限制
     */
    public int OpenTimes;
    /**
     *用户身份<br>
     * 0 -- 普通用户<br>
     * 1 -- 管理员
     */
    public int Identity;
    /**
     *卡片类型<br>
     * 0 -- 普通卡<br>
     * 1 -- 常开
     */
    public int CardType;
    /**
     *卡片状态<br>
     * 0：正常状态；<br>
     * 1：挂失；<br>
     * 2：黑名单；<br>
     * 3：已删除
     */
    public  int CardStatus;
    /**
     *出入标记<br>
     * 0  出入有效<br>
     * 1  入有效<br>
     * 2  出有效
     */
    public int EnterStatus;
    /**
     *人员姓名
     */
    public String PName;
    /**
     *人员编号
     */
    public String PCode;
    /**
     *人员部门
     */
    public String Dept;
    /**
     *人员职务
     */
    public  String Job;
    /**
     *最近验证时间
     */
    public  Calendar RecordTime;
    /**
     *是否有人脸特征码
     */
    public  boolean IsFaceFeatureCode;
    /**
     *节假日权限
     */
    protected   byte[] Holiday;
    /**
     *是否有指纹特征码<br>
     * 每个位表示一个指纹，一个人有10个指纹<br>
     * Bit0--指纹1，0-没有；1--有<br>
     * ...........<br>
     *  bit9--指纹10<br>
     */
    private   int FingerprintFeatureCodeCout;

    /**
     * 是否有指纹特征码
     * 每个位表示一个指纹，一个人有10个指纹<br>
     * Bit0--指纹1，0-没有；1--有<br>
     * ...........<br>
     * bit9--指纹10<br>
     * @return 指纹值（需要转换成位如：1111111111）
     */
    public  int getFingerprintFeatureCodeCout(){
        return FingerprintFeatureCodeCout;
    }
    /**
     * 设置指定序号的节假日开关状态
     * @param iIndex 取值范围 1-30
     * @return  开关状态 开关true 表示启用，false 表示禁用
     */
    public boolean GetHolidayValue(int iIndex) {
        if (iIndex <= 0 || iIndex > 32) {
            throw new IllegalArgumentException("iIndex= 1 -- 32");
        }
        iIndex -= 1;
        //计算索引所在的字节位置
        int iByteIndex = iIndex / 8;
        int iBitIndex = iIndex % 8;
        int iByteValue = Holiday[iByteIndex] & 0x000000ff;
        int iMaskValue = (int) Math.pow(2, iBitIndex);
        iByteValue = iByteValue & iMaskValue;
        if (iBitIndex > 0) {
            iByteValue = iByteValue >> (iBitIndex);
        }
        return iByteValue == 1;

    }
    /**
     * 设置指定序号的节假日开关状态
     *
     * @param iIndex 取值范围 1-30
     * @param bUse 开关状态 开关true 表示启用，false 表示禁用
     */
    public void SetHolidayValue(int iIndex, boolean bUse) {
        if (iIndex <= 0 || iIndex > 32) {
            throw new IllegalArgumentException("iIndex= 1 -- 32");
        }
        if (bUse == GetHolidayValue(iIndex)) {
            return;
        }
        iIndex -= 1;
        int iByteIndex = iIndex / 8;
        int iBitIndex = iIndex % 8;
        int iByteValue = Holiday[iByteIndex] & 0x000000ff;
        int iMaskValue = (int) Math.pow(2, iBitIndex);
        if (bUse) {
            iByteValue = iByteValue | iMaskValue;
        } else {
            iByteValue = iByteValue ^ iMaskValue;
        }

        Holiday[iByteIndex] = (byte) iByteValue;
    }
/**
 * 人员信息
 */
    public Person(){
        UserCode = 0;
        CardData = 0;
        Password = "";
        Expiry =  Calendar.getInstance();
        Expiry.add(Calendar.YEAR, 30);
        TimeGroup = 1;
        OpenTimes = 65535;
        Identity = 0;
        CardType = 0;
        CardStatus = 0;
        EnterStatus = 0;
        PName =  "";
        PCode = "";
        Dept =  "";
        Job =  "";
        RecordTime =Calendar.getInstance();
        IsFaceFeatureCode = false;
        Holiday = new byte[] { (byte)255, (byte)255, (byte)255, (byte)255 };
        FingerprintFeatureCodeCout = 0;
    }
    @Override
    public int compareTo(Person o) {
        if (o.UserCode == UserCode)
        {
            return 0;
        }
        else if (UserCode < o.UserCode)
        {
            return -1;
        }
        else if (UserCode > o.UserCode)
        {
            return 1;
        }
        else
        {
            return 0;
        }
    }
    /**
     * 请勿调用
     * @param buf
     * @throws UnsupportedEncodingException 
     */
    public void getBytes(ByteBuf buf) throws UnsupportedEncodingException {
        buf.writeInt(UserCode);
        buf.writeLong(CardData);
        Password = StringUtil.FillHexString(Password, 8, "F", true);
        long pwd = Long.parseLong(Password, 16);
        buf.writeInt((int) pwd);
        byte[] btTime = new byte[6];
        TimeUtil.DateToBCD_yyMMddhhmm(btTime, Expiry);
        buf.writeBytes(btTime, 0, 5);
        buf.writeByte(TimeGroup);
        buf.writeShort(OpenTimes);
        buf.writeByte(Identity);
        buf.writeByte(CardType);
        buf.writeByte(CardStatus);
        ByteUtil.writeString(buf, PName,30);
        ByteUtil.writeString(buf, PCode,30);
        ByteUtil.writeString(buf, Dept,30);
        ByteUtil.writeString(buf, Job,30);
        buf.writeBytes(Holiday, 0, 4);
        buf.writeByte(EnterStatus);
        byte[] RecordTime = new byte[]{0, 0, 0, 0,0,0};
        buf.writeBytes(RecordTime);
        buf.writeBoolean(IsFaceFeatureCode);
        buf.writeShort(FingerprintFeatureCodeCout);
    }
    /**
     * 请勿调用
     * @param buf
     * @throws UnsupportedEncodingException 
     */
    public  void setBytes(ByteBuf buf) throws UnsupportedEncodingException {
        UserCode = buf.readInt();
        CardData = buf.readLong();
        byte[] btData = new byte[4];
        buf.readBytes(btData, 0, 4);
        Password = ByteUtil.ByteToHex(btData);
        byte[] btTime = new byte[6];
        buf.readBytes(btTime, 0, 5);
        Expiry = TimeUtil.BCDTimeToDate_yyMMddhhmm(btTime);
        TimeGroup = buf.readByte();
        OpenTimes = buf.readUnsignedShort();
        Identity = buf.readByte();
        CardType = buf.readByte();
        CardStatus = buf.readByte();
        PName = ByteUtil.getString(buf, 30);
        PCode = ByteUtil.getString(buf, 30);
        Dept = ByteUtil.getString(buf, 30);
        Job = ByteUtil.getString(buf, 30);
        Holiday = new byte[4];
        buf.readBytes(Holiday, 0, Holiday.length);
        EnterStatus = buf.readByte();
        byte[] btRecord = new byte[6];
        buf.readBytes(btRecord, 0, 6);
        RecordTime = TimeUtil.BCDTimeToDate_yyMMddhhmmss(btRecord);
        IsFaceFeatureCode = buf.readBoolean();
        FingerprintFeatureCodeCout = buf.readUnsignedShort();
    }
}

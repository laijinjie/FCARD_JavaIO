/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Door.Access.Door8800.Command.Data;

import Door.Access.Data.INData;
import Door.Access.Util.ByteUtil;
import Door.Access.Util.StringUtil;
import Door.Access.Util.TimeUtil;
import io.netty.buffer.ByteBuf;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * 卡片权限详情
 *
 * @author 赖金杰
 */
public class CardDetail implements INData, Comparable<CardDetail> {

    @Override
    public int compareTo(CardDetail o) {
        if (o.CardData.equals(CardData)) {
            return 0;
        } else {
            return -1;
        }

        //return Long.compare(CardData, o.CardData);
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof CardDetail) {
            return compareTo((CardDetail) o) == 0;
        } else {
            return false;
        }

    }

    @Override
    public int GetDataLen() {
        return 0x21;//33字节
    }

    public void SetCardData(String value) throws Exception {
        /**/
        if (value == null || value.length() == 0) {
            throw new Exception("卡号不能为空");
        }

        if (!StringUtil.IsNum(value)) {
            throw new Exception("卡号不是数字格式");
        }

        if (value.length() > 10) {
            throw new Exception("卡号长度过长");
        }

        //BigInteger biLongMax = new BigInteger("18446744073709551615");
        long lCardMax = 0xffffffffL;
        long biCardData = Long.valueOf(value);

        if (lCardMax < biCardData) {
            throw new Exception("卡号超过最大值");
        }

        CardData = value;
    }

    /// <summary>
    /// 将卡号序列化并写入buf中
    /// </summary>
    /// <param name="data"></param>
    public void WriteCardData(ByteBuf data) throws Exception {
        data.writeByte(0);
        if (CardData == null ) {
            data.writeInt(0);
        }
        else
        {
            long biCardData = Long.valueOf(CardData);
            data.writeInt((int)biCardData);
        }

    }

    /// <summary>
    /// 从buf中读取卡号
    /// </summary>
    /// <param name="data"></param>
    public void ReadCardData(ByteBuf data) {
        data.readByte();
        CardData = String.valueOf(data.readUnsignedInt());
    }

    @Override
    public void SetBytes(ByteBuf data) {
        ReadCardData(data);

        byte[] btData = new byte[4];
        data.readBytes(btData, 0, 4);
        Password = ByteUtil.ByteToHex(btData);

        byte[] btTime = new byte[6];
        data.readBytes(btTime, 0, 5);
        Expiry = TimeUtil.BCDTimeToDate_yyMMddhhmm(btTime);

        data.readBytes(btData, 0, 4);
        for (int i = 0; i < 4; i++) {
            TimeGroup[i] = btData[i];
        }

        OpenTimes = data.readUnsignedShort();

        int bData = data.readUnsignedByte();//特权
        Door = bData >> 4;
        bData = bData & 15;
        Privilege = bData & 7;
        HolidayUse = (bData & 8) == 8;
        CardStatus = data.readByte();

        data.readBytes(btData, 0, 4);
        for (int i = 0; i < 4; i++) {
            Holiday[i] = btData[i];
        }

        EnterStatus = data.readByte();
        data.readBytes(btTime, 0, 6);
        RecordTime = TimeUtil.BCDTimeToDate_yyMMddhhmmss(btTime);
        return;
    }

    @Override
    public ByteBuf GetBytes() {

        return null;
    }

    public void GetBytes(ByteBuf data) {
        try {
            WriteCardData(data);
        } catch (Exception e) {
            return;
        }
        Password = StringUtil.FillHexString(Password, 8, "F", true);
        long pwd = Long.parseLong(Password, 16);
        data.writeInt((int) pwd);

        byte[] btTime = new byte[6];
        TimeUtil.DateToBCD_yyMMddhhmm(btTime, Expiry);
        data.writeBytes(btTime, 0, 5);

        data.writeBytes(TimeGroup, 0, 4);

        data.writeShort(OpenTimes);

        int bData = (Door << 4) + Privilege;//特权
        if (HolidayUse) {
            bData = bData | 8;
        }
        data.writeByte(bData);
        data.writeByte(CardStatus);

        data.writeBytes(Holiday, 0, 4);

        data.writeByte(EnterStatus);

        TimeUtil.DateToBCD_yyMMddhhmmss(btTime, RecordTime);
        data.writeBytes(btTime, 0, 6);

    }

    public String GetCardData() {
        return CardData;
    }
    /**
     * 卡号，取值范围 0x1-0xFFFFFFFF
     */
    protected String CardData;
    /**
     * 卡密码,无密码不填。密码是4-8位的数字。
     */
    public String Password;
    /**
     * 截止日期，最大2089年12月31日
     */
    public Calendar Expiry;
    /**
     * 开门时段<br/>
     * 1-4门的开门时段；时段取值范围：1-64<br/>
     * TimeGroup[0] -- 1门的时段<br/>
     * TimeGroup[1] -- 2门的时段<br/>
     * TimeGroup[2] -- 3门的时段<br/>
     * TimeGroup[3] -- 4门的时段<br/>
     */
    protected byte[] TimeGroup;
    /**
     * 有效次数,取值范围：0-65535;<br.>
     * 0表示次数用光了。65535表示不受限制
     */
    public int OpenTimes;

    /**
     * 开门权限<br/>
     * 1-4门的开门权限；false--无权，true--有权开门<br/>
     * bit0 -- 1门的权限<br/>
     * bit1 -- 2门的权限<br/>
     * bit2 -- 3门的权限<br/>
     * bit3 -- 4门的权限<br/>
     */
    protected int Door;

    /**
     * 特权, 
     * 普通卡：普通开门卡
     * 首卡：当开启首卡功能时，在某个时段（周时段，天时段）如果首卡不刷卡，其他卡无法使用<br/>
     * 常开卡：在非常开时间刷卡时，可以保持门常开状态<br/>
     * 巡更卡，不会开门，只要刷卡记录<br/>
     * 防盗设置卡，刷卡时会启动防盗状态
     * <ul>
     * <li>0 &emsp; 普通卡      </li>
     * <li>1 &emsp; 首卡        </li>
     * <li>2 &emsp; 常开        </li>
     * <li>3 &emsp; 巡更        </li>
     * <li>4 &emsp; 防盗设置卡  </li>
     * </ul>
     */
    protected int Privilege;

    /**
     * 卡片状态<br/>
     * 0：正常状态；1：挂失；2：黑名单
     */
    public byte CardStatus;

    /**
     * 节假日权限
     */
    protected byte[] Holiday;

    /**
     * 使用节假日限制功能,节假日禁止开门
     */
    public boolean HolidayUse;

    /**
     * 出入标记；
     */
    public int EnterStatus;

    /**
     * 最近一次读卡的记录时间
     */
    public Calendar RecordTime;

    public CardDetail() {
        CardData = "0";
        Password = null;
        Expiry = null;
        TimeGroup = new byte[4];
        Door = 0;
        Privilege = 0;
        CardStatus = 0;
        Holiday = new byte[]{(byte) 255, (byte) 255, (byte) 255, (byte) 255};
        RecordTime = null;
        EnterStatus = 0;
        HolidayUse = false;
    }

    /**
     * 获取指定门的开门时段号
     *
     * @param iDoor 取值范围1-4
     * @return 开门时段号
     */
    public int GetTimeGroup(int iDoor) {
        if (iDoor < 0 || iDoor > 4) {

            throw new IllegalArgumentException("Door 1-4");
        }
        return TimeGroup[iDoor - 1];
    }

    /**
     * 设置指定门的开门时段号
     *
     * @param iDoor 门号，取值范围：1-4
     * @param iNum 开门时段号，取值范围：1-64
     */
    public void SetTimeGroup(int iDoor, int iNum) {
        if (iDoor < 0 || iDoor > 4) {

            throw new IllegalArgumentException("Door 1-4");
        }

        if (iNum < 0 || iNum > 64) {

            throw new IllegalArgumentException("Num 1-64");
        }
        TimeGroup[iDoor - 1] = (byte) iNum;
    }

    /**
     * 获取指定门是否有权限
     *
     * @param iDoor 门号，取值范围：1-4
     * @return true 有权限，false 无权限。
     */
    public boolean GetDoor(int iDoor) {
        if (iDoor < 0 || iDoor > 4) {

            throw new IllegalArgumentException("Door 1-4");
        }
        iDoor -= 1;

        int iBitIndex = iDoor % 8;
        int iMaskValue = (int) Math.pow(2, iBitIndex);
        int iByteValue = Door & iMaskValue;
        if (iBitIndex > 0) {
            iByteValue = iByteValue >> (iBitIndex);
        }
        return iByteValue == 1;
    }

    /**
     * 设置指定门是否有权限
     *
     * @param iIndex 门号，取值范围：1-4
     * @param bUse true 有权限，false 无权限。
     */
    public void SetDoor(int iDoor, boolean bUse) {
        if (iDoor < 0 || iDoor > 4) {

            throw new IllegalArgumentException("Door 1-4");
        }

        if (bUse == GetDoor(iDoor)) {
            return;
        }

        iDoor -= 1;
        int iBitIndex = iDoor % 8;
        int iMaskValue = (int) Math.pow(2, iBitIndex);
        if (bUse) {
            Door = Door | iMaskValue;
        } else {
            Door = Door ^ iMaskValue;
        }
    }

    /**
     * 普通开门卡
     */
    public boolean IsNormal() {
        return Privilege == 0;
    }

    /**
     * 普通开门卡--五特权开门卡
     */
    public void SetNormal() {
        Privilege = 0;
    }

    /**
     * 首卡特权卡
     */
    public boolean IsPrivilege() {
        return Privilege == 1;
    }

    /**
     * 首卡特权卡
     */
    public void SetPrivilege() {
        Privilege = 1;
    }

    /**
     * 常开特权卡
     */
    public boolean IsTiming() {
        return Privilege == 2;
    }

    /**
     * 常开特权卡
     */
    public void SetTiming() {
        Privilege = 2;
    }

    /**
     * 巡更卡
     */
    public boolean IsGuardTour() {
        return Privilege == 3;
    }

    /**
     * 巡更卡
     */
    public void SetGuardTour() {
        Privilege = 3;
    }

    /**
     * 防盗设置卡
     */
    public boolean IsAlarmSetting() {
        return Privilege == 4;
    }

    /**
     * 防盗设置卡
     */
    public void SetAlarmSetting() {
        Privilege = 4;
    }

    /**
     * 获取指定序号的节假日开关状态
     *
     * @param iIndex 取值范围 1-30
     * @return 开关状态 开关true 表示启用，false 表示禁用
     */
    public boolean GetHolidayValue(int iIndex) {
        if (iIndex <= 0 || iIndex > 30) {
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
        if (iIndex <= 0 || iIndex > 30) {
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
     * 使用折半查询方式搜索卡片集合
     *
     * @param list 已经过排序的卡片集合
     * @param SearchCard 需要搜索的卡片卡号
     * @return 在集合中的索引号
     */
    public static int SearchCardDetail(ArrayList<CardDetail> list, String SearchCard) {
        CardDetail search = new CardDetail();
        try {
            search.SetCardData(SearchCard);
            return SearchCardDetail(list, search);
        } catch (Exception e) {
             return -1;
        }
    }

    /**
     * 使用折半查询方式搜索卡片集合
     *
     * @param list 已经过排序的卡片集合
     * @param search 需要搜索的卡片卡号
     * @return 在集合中的索引号
     */
    public static int SearchCardDetail(ArrayList<CardDetail> list, CardDetail search) {
        int max, min, mid;
        max = list.size() - 1;
        min = 0;
        while (min <= max) {
            mid = (max + min) >> 1;
            CardDetail cd = list.get(mid);
            int num = cd.compareTo(search);
            if (num > 0) {
                max = mid - 1;
            } else if (num < 0) {
                min = mid + 1;
            } else {
                return mid;
            }
        }
        return -1;

    }

}

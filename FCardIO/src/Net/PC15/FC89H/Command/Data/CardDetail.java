/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Net.PC15.FC89H.Command.Data;

import Net.PC15.Util.ByteUtil;
import Net.PC15.Util.StringUtil;
import Net.PC15.Util.TimeUtil;
import io.netty.buffer.ByteBuf;
import java.math.BigInteger;
import java.util.ArrayList;

/**
 * 针对FC89H使用，卡片权限详情
 *
 * @author 徐铭康
 */
public class CardDetail extends Net.PC15.FC8800.Command.Data.CardDetail {
    public CardDetail() {
        CardDataHEX = null;
        Password = null;
        Expiry = null;
        TimeGroup = new byte[4];
        Door = 0;
        Privilege = 0;
        CardStatus = 0;
        Holiday = new byte[]{(byte)255,(byte)255,(byte)255,(byte)255};
        RecordTime = null;
        EnterStatus = 0;
        HolidayUse = false;
    }
    
    //@Override
    public int compareTo(Net.PC15.FC89H.Command.Data.CardDetail o) {
        if (o.CardDataHEX.equals(CardDataHEX) ) {
            return 0;
        } else {
            return -1;
        }

        //return Long.compare(CardData, o.CardData);
    }
    
    @Override
    public boolean equals(Object o)
    {
        if(o instanceof Net.PC15.FC89H.Command.Data.CardDetail)
        {
            return compareTo((Net.PC15.FC89H.Command.Data.CardDetail)o)==0;
        }else
        {
            return false;
        }
        
    }

    @Override
    public int GetDataLen() {
        return 0x25;//33字节
    }

    /*
    public CardDetail(String data) {
        this();
        CardDataHEX = data;
        CardDataStr = data;
    }
    */
    
    /**
     * 16进制卡号
     */
    protected String CardDataHEX;
    
    /**
     * 10进制卡号
     */
    protected String CardDataStr;
    
    /**
     * 获取16进制卡号
     */
    public String GetCardDataHEX() {
        return CardDataHEX;
    }
    
    /**
     * 获取10进制卡号
     */
    public String GetCardData() {
        return CardDataStr;
    }
    
    public void SetCardDataHEX(String value) throws Exception{
        if (value == null || value.length() == 0 ) {
            System.out.println("ERROR! 卡号不能为空!");
            throw new Exception("卡号不能为空");
        }
        int length = value.length();
        
        if (!Net.PC15.Util.StringUtil.CanParseInt(value)) {
            System.out.println("ERROR! 卡号不是数字格式!");
            throw new Exception("卡号不是数字格式");
        }
        String maxHex = new BigInteger(value,10).toString(16);
        if (maxHex == "ffffffffffffffff") {
            System.out.println("ERROR! 卡号超过最大值!");
            throw new Exception("卡号超过最大值");
        }
        CardDataHEX = value;
    }
    
    /**
     * 读数据
     */
    @Override
    public void SetBytes(ByteBuf data) {
        data.readByte();
        //测试完整data
        //byte[] btCardData = new byte[36];
        //data.readBytes(btCardData, 0, 36);
        
        
        byte[] btCardData = new byte[8];
        data.readBytes(btCardData, 0, 8);
        CardDataHEX = ByteUtil.ByteToHex(btCardData);
        
            CardDataHEX = Net.PC15.Util.StringUtil.LTrim(CardDataHEX,'0');
            
            //CardDataHEX = Net.PC15.Util.StringUtil.HexStr2Str(CardDataHEX,16);
            
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
        
    }
    
    /**
     * 写数据
     */
    @Override
    public void GetBytes(ByteBuf data) throws Exception{
        
        if (CardDataHEX == null) {
            System.out.println("ERROR! 卡号为空!");
            throw new Exception("卡号为空");
        }
        int length = CardDataHEX.length();
        if (CardDataHEX.length() == 0 || length < 5 || length > 16) {
            System.out.println("ERROR! 卡号长度不正确!");
            throw new Exception("卡号长度不正确");
        }
        if (!Net.PC15.Util.StringUtil.CanParseInt(CardDataHEX)) {
            System.out.println("ERROR! 卡号不是数字格式!");
            throw new Exception("卡号不是数字格式");
        }
         data.writeByte(0);
         
         //传16进制
        String CardDataHEX2 = Net.PC15.Util.StringUtil.FillString(CardDataHEX, 16, "0", false);
        Net.PC15.Util.StringUtil.HextoByteBuf(CardDataHEX2,data);
        
        //传10进制
        //data.writeBytes(CardDataHEX.getBytes());
        
        Password = StringUtil.FillHexString(Password, 8, "F", true);
        long pwd = Long.parseLong(Password, 16);
        data.writeInt((int)pwd);

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

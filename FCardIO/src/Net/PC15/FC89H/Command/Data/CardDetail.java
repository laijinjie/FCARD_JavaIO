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
    
    
    /**
     * 获取10进制卡号
     */
    public String GetCardData() {
        return CardData;
    }
    
    public void SetCardDataHEX(String value) throws Exception{
        /**/
        if (value == null || value.length() == 0 ) {
            System.out.println("ERROR! 卡号不能为空!");
            throw new Exception("卡号不能为空");
        }
        
        if (!Net.PC15.Util.StringUtil.CanParseInt(value)) {
            System.out.println("ERROR! 卡号不是数字格式!");
            throw new Exception("卡号不是数字格式");
        }
        String maxHex = new BigInteger(value,10).toString(16);
        BigInteger biLongMax = new BigInteger("18446744073709551615");
	BigInteger biCardData = new BigInteger(value);
                
        if (biLongMax.compareTo(biCardData) <= 0) {
            System.out.println("ERROR! 卡号超过最大值!");
            throw new Exception("卡号超过最大值");
        }
        
        CardData = value;
    }
    
    @Override
    public void WriteCardData(ByteBuf data)  throws Exception{
        data.writeByte(0);
         if (CardData == null || CardData.length() == 0 ) {
            System.out.println("ERROR! 卡号不能为空!");
            throw new Exception("卡号不能为空");
        }
        
        if (!Net.PC15.Util.StringUtil.CanParseInt(CardData)) {
            System.out.println("ERROR! 卡号不是数字格式!");
            throw new Exception("卡号不是数字格式");
        }
        BigInteger biLongMax = new BigInteger("18446744073709551615");
	BigInteger biCardData = new BigInteger(CardData);
                
        if (biLongMax.compareTo(biCardData) <= 0) {
            System.out.println("ERROR! 卡号超过最大值!");
            throw new Exception("卡号超过最大值");
        }
        
        String CardDataHex = new BigInteger(CardData,10).toString(16);
        //写入 
        CardDataHex = Net.PC15.Util.StringUtil.FillString(CardDataHex, 16, "0", false);
        Net.PC15.Util.StringUtil.HextoByteBuf(CardDataHex,data);
    }
    
    
    @Override
    public void ReadCardData(ByteBuf data){
            data.readByte();
            byte[] btCardData = new byte[8];
            data.readBytes(btCardData, 0, 8);
            CardData = ByteUtil.ByteToHex(btCardData);
            CardData = Net.PC15.Util.StringUtil.LTrim(CardData,'0');
            CardData = Net.PC15.Util.StringUtil.HexStr2Str(CardData,16);
    }
        
    /**
     * 读数据
     */
    @Override
    public void SetBytes(ByteBuf data) {
        //测试完整data
        //byte[] btCardData = new byte[36];
        //data.readBytes(btCardData, 0, 36);
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
        
    }
    
    /**
     * 写数据
     */
    @Override
    public void GetBytes(ByteBuf data){
        try {
            WriteCardData(data);
        }
        catch(Exception e){
            return;
        }
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

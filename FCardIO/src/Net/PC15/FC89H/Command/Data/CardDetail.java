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

    
    public CardDetail(String data) {
        this();
        CardDataHEX = data;
        CardData = Long.valueOf(data);
    }
    
    /**
     * 
     */
    private String CardDataHEX;
    
    @Override
    public void SetBytes(ByteBuf data) {
        data.readByte();
        //CardData = data.readUnsignedInt();
        byte[] btCardData = new byte[4];
        data.readBytes(btCardData, 0, 4);
        CardDataHEX = ByteUtil.ByteToHex(btCardData);
        CardData = Long.valueOf(CardDataHEX);
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
    public void GetBytes(ByteBuf data) {
         data.writeByte(0);
        //data.writeInt((int) CardData);
        
        Net.PC15.Util.StringUtil.HextoByteBuf(CardDataHEX,data);
        
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
}

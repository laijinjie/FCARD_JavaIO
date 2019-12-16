/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package testio;

import Net.PC15.FC8800.Command.System.Parameter.WriteSN_Parameter;
import Net.PC15.Command.CommandDetail;
import Net.PC15.Command.INCommandRuntime;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import Net.PC15.Util.StringUtil;
import Net.PC15.FC8800.FC8800Identity;
import Net.PC15.Connector.E_ControllerType;
import Net.PC15.Connector.TCPClient.TCPClientDetail;
import Net.PC15.FC8800.Command.*;
import Net.PC15.FC8800.Command.Data.*;
import Net.PC15.FC8800.Packet.*;
import Net.PC15.Packet.INPacketDecompile;
import Net.PC15.Packet.INPacketModel;
import Net.PC15.Util.ByteUtil;
import Net.PC15.Util.TimeUtil;
import Net.PC15.Util.UInt32Util;
import java.lang.StringBuilder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Random;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 *
 * @author 赖金杰
 */
public class TestIO {

    public static void TestBytebuf() {
        PooledByteBufAllocator allocator = new PooledByteBufAllocator(false);
        ByteBuf buf = allocator.buffer(4);
        System.out.println(buf.refCnt());
        long a = 0x11111111;
        int b = (int) a;

        buf.writeInt(b);
        buf.markReaderIndex();
        long value = buf.readUnsignedInt();
        buf.resetReaderIndex();
        System.out.println(value);
        System.out.println(ByteBufUtil.hexDump(buf));
        buf.release();//释放缓冲区到缓冲池中。
        buf.release();
        System.out.println(buf.refCnt());
    }

    public static ByteBuf a(ByteBuf input) {
        //a()方法中 input方法返回,input 还会继续使用,则由方法c()释放
        input.writeByte(42);
        return input;
    }

    public static ByteBuf b(ByteBuf input) {
        try {
            //input 不在使用,则由方法b()直接释放
            ByteBuf output = input.alloc().heapBuffer(input.readableBytes() + 1);
            output.writeBytes(input);
            output.writeByte(42);
            return output;
        } finally {
            input.release();
        }
    }

    public static void c(ByteBuf input) {
        System.out.println(input);
        input.release();
    }

    public static void main(String[] args) {
        /*String hex = "7E190FD7A046432D38393230413237303630303130FFFFFFFF310B020000000100347E";
        String str = "abc ";
        System.out.println(StringUtil.IsAscii(str));
        boolean b=StringUtil.IsHex(hex);
        if(b)
        {
            byte[] buf=StringUtil.Hex2Byte(hex);

            System.out.println(StringUtil.ByteToHex(buf));
            System.out.println(StringUtil.BytesToString(buf));
        }

        CommandDetail d = GetConnInfo();*/
 /* E_WeekDay FistWeek=E_WeekDay.Thursday;
        byte[] WeekList=new byte[7];
        
        int lBeginIndex = FistWeek.getValue();
        int iEndIndex = 6 - lBeginIndex;
        for (int i = 0; i <= iEndIndex; i++) {
            WeekList[i] = (byte)(lBeginIndex + i);
        }
        if (lBeginIndex != 0) {
            iEndIndex += 1;
            lBeginIndex = 0;
            for (int i = iEndIndex; i <= 6; i++) {
                WeekList[i] = (byte)(lBeginIndex);
                lBeginIndex += 1;
            }
        }
        System.out.println(ByteUtil.BytesToString(WeekList));
         */
         /*
         long lpwd = Long.parseLong("9999ffff", 16);
         int pwd = (int)lpwd;
         System.out.println(lpwd);
         
         ByteBuf buf=ByteUtil.ALLOCATOR.buffer(4);
         buf.writeInt(pwd);
         System.out.println(buf.readUnsignedInt());
         */
        //TestSearchCard();
        //System.out.println(Math.pow(2, 8));

        //TestCommand();
        
        
    }

    public static void TestSearchCard() {
        int iArrSize = 2002995;
        ArrayList<CardDetail> CardList = new ArrayList<CardDetail>(iArrSize);
        Random rnd = new Random();
        int max = 5000000;
        int min = 100;

        for (int i = 0; i < iArrSize; i++) {
            long card = rnd.nextInt(max) % (max - min + 1) + min;
            //System.out.println("卡号：" + card);
            CardList.add(new CardDetail(card));
        }

        Collections.sort(CardList);

        int iSearch = 0;
        //System.out.println("序列号：" + iSearch);
        int len = CardList.size();
        for (int i = 0; i < len; i++) {
            CardDetail cd = CardList.get(i);
            iSearch = CardDetail.SearchCardDetail(CardList, cd.CardData);
            if (iSearch != i) {
                CardDetail cd1 = CardList.get(iSearch);
                if (cd1.CardData == cd.CardData) {
                    iSearch = i;//检查是否值重复
                }
                //可能是由于有重复
                /* if (iSearch < i) {
                    for (int j = iSearch + 1; j < len; j++) {
                        CardDetail cd1 = CardList.get(j);
                        if (cd1.compareTo(cd) != 0) {
                            break;
                        } else {
                            iSearch = j;
                            if (iSearch == i) {
                                break;
                            }
                        }
                    }
                } else {
                    for (int j = iSearch - 1; j >= 0; j--) {
                        CardDetail cd1 = CardList.get(j);
                        if (cd1.compareTo(cd) != 0) {
                            break;
                        } else {
                            iSearch = j;
                            if (iSearch == i) {
                                break;
                            }
                        }
                    }
                }*/

                if (iSearch != i) {
                    System.out.println("找不到的序列号：" + iSearch);
                }

            }
        }
    }

    public static void TestDecompile() {
        byte[] bDataHex = StringUtil.HexToByte("7e0496440946432d38393430413436303630303037ffffffff3703000000014e0000000a0000ce98d348768287171115000001010101fffff800ffffffff000000000000000000ceb79845734321171115000001010101fffff800ffffffff000000000000000000cede6435936599171115000001010101fffff800ffffffff000000000000000000cf42ba43677987171115000001010101fffff800ffffffff000000000000000000cfaf1621048578171115000001010101fffff800ffffffff000000000000000000cfd5e211250856171115000001010101fffff800ffffffff000000000000000000d03a3818992244171115000001010101fffff800ffffffff000000000000000000d07f02c995049445171115000001010101fffff800ffffffff000000000000000000d0cd6051846960171115000001010101fffff800ffffffff000000000000000000d131b759588347171115000001010101fffff800ffffffff00000000000000dd7e");
        FC8800Decompile decompile = new FC8800Decompile();
        ByteBuf dataBuf = ByteUtil.ALLOCATOR.buffer(bDataHex.length);
        dataBuf.writeBytes(bDataHex);
        ArrayList<INPacketModel> oRetPack = new ArrayList<INPacketModel>(10);
        boolean ret = decompile.Decompile(dataBuf, oRetPack);
        if (ret) {
            System.out.println("解析成功，解析到的正确指令数：" + oRetPack.size());
        }
    }

    /**
     * 测试卡片的详情的拆包和打包
     */
    public static void TestCardDetail() {
        String sHex = "00004BE175666333FF17112212300307070800C6BA00EF9BF33F00000000000000";
        byte[] byteHex = StringUtil.HexToByte(sHex);
        ByteBuf bBuf = ByteUtil.ALLOCATOR.buffer(100);
        bBuf.writeBytes(byteHex);
        CardDetail cd = new CardDetail();
        cd.SetBytes(bBuf);
        StringBuilder builder = new StringBuilder(1000);
        builder.append("卡号：");
        builder.append(cd.CardData);
        builder.append("，密码：");
        builder.append(cd.Password.replaceAll("F", ""));
        builder.append("，有效期：");
        builder.append(TimeUtil.FormatTime(cd.Expiry));
        builder.append("\n有效次数：");
        builder.append(cd.OpenTimes);
        builder.append("，特权：");
        if (cd.IsNormal()) {
            builder.append("无");
        } else if (cd.IsPrivilege()) {
            builder.append("首卡特权卡");
        } else if (cd.IsTiming()) {
            builder.append("常开特权卡");
        } else if (cd.IsGuardTour()) {
            builder.append("巡更卡");
        } else if (cd.IsAlarmSetting()) {
            builder.append("防盗设置卡");
        }
        builder.append("，卡状态：");
        switch (cd.CardStatus) {
            case 0:
                builder.append("正常");
                break;
            case 1:
                builder.append("挂失");
                break;
            case 2:
                builder.append("黑名单");
                break;
            default:
                builder.append(cd.CardStatus);
                break;

        }

        builder.append("\n权限和时段：");
        for (int i = 1; i <= 4; i++) {
            builder.append("门");
            builder.append(i);
            builder.append("：");
            if (cd.GetDoor(i)) {
                builder.append("有权限");
            } else {
                builder.append("无权限");
            }

            builder.append("；时段号");
            builder.append(cd.GetTimeGroup(i));
            builder.append("；");
        }
        builder.append("\n节假日:");
        builder.append(cd.HolidayUse);
        builder.append("，受限制的节假日开关列表：\n");

        for (int i = 1; i <= 30; i++) {
            builder.append(i);
            builder.append("：");
            builder.append(cd.GetHolidayValue(i));
            builder.append("；");
            if (i == 15) {
                builder.append("\n");
            }
        }

        System.out.println(builder.toString());

        bBuf.clear();
        cd.GetBytes(bBuf);
        String CardDetailHex = ByteBufUtil.hexDump(bBuf);
        /*System.out.println(sHex.toLowerCase());
        System.out.println(CardDetailHex.toLowerCase());*/
        if (sHex.toLowerCase().equals(CardDetailHex.toLowerCase())) {
            System.out.println("转换正确");

        } else {
            System.out.println("转换错误!");
        }

    }

    public static void TestCommand() {
        String newSN = "FC-8920A27060010";
        WriteSN_Parameter par = new WriteSN_Parameter(GetConnInfo(), newSN);
        FC8800Command cmd = new Net.PC15.FC8800.Command.System.WriteSN(par);
        cmd.Release();
    }

    public static CommandDetail GetConnInfo() {
        CommandDetail detial = new CommandDetail();
        detial.Identity = new FC8800Identity("FC-8920A27060010", "ffffffff", E_ControllerType.FC8900);
        TCPClientDetail tcp = new TCPClientDetail("192.168.1.30", 8000);

        detial.Connector = tcp;
        return detial;
    }

    public static void TestFC8800Packet() {
        FC8800PacketCompile packetCompile;
        String sn = "FC-8940A46060007";
        long PWD = UInt32Util.UINT32_MAX;
        packetCompile = new FC8800PacketCompile(sn, PWD, (short) 1, (short) 2, (short) 0);
        System.out.println(ByteBufUtil.hexDump(packetCompile.GetPacketData()));
        packetCompile.Release();

        ByteBuf dataBuf = ByteUtil.ALLOCATOR.buffer(4);
        dataBuf.writeInt(0x01010001);
        packetCompile = new FC8800PacketCompile(sn, PWD, (short) 3, (short) 3, (short) 0, (short) 4, dataBuf);
        System.out.println(ByteBufUtil.hexDump(packetCompile.GetPacketData()));
        packetCompile.Release();
        dataBuf = ByteUtil.ALLOCATOR.buffer(4);
        dataBuf.writeInt(0x02020202);
        dataBuf.release();
        dataBuf = null;

        StringBuilder DataHex = new StringBuilder(1024);
        DataHex.append("7EFFFFFFFF46432D38393430413436303630303037FFFFFFFF190400000000080117092610331601217E");
        DataHex.append("7EFFFFFFFF46432D38393430413436303630303037FFFFFFFF190400000000080217092610331601227E");
        DataHex.append("7EFFFFFFFF46432D38393430413436303630303037FFFFFFFF190400000000080417092610331601247E");
        DataHex.append("7E7E7E5555");
        DataHex.append("46432D38393430413436303630303037");
        DataHex.append("7EBA7B21A546432D38393430413436303630303037FFFFFFFF210100000000007C7E");
        DataHex.append("7EFFFFFFFF46432D38393430413436303630303037FFFFFFFF190400000000080117092610331601217E");
        DataHex.append("46432D38393430413436303630303037FFFFFFFF");

        byte[] bDataHex = StringUtil.HexToByte(DataHex.toString());

        FC8800Decompile decompile = new FC8800Decompile();
        dataBuf = ByteUtil.ALLOCATOR.buffer(bDataHex.length);
        dataBuf.writeBytes(bDataHex);
        ArrayList<INPacketModel> oRetPack = new ArrayList<INPacketModel>(10);
        boolean ret = decompile.Decompile(dataBuf, oRetPack);
        if (ret) {
            System.out.println("解析成功，解析到的正确指令数：" + oRetPack.size());
        }
    }

}

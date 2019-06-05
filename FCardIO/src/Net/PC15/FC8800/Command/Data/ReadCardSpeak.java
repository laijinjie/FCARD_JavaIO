/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Net.PC15.FC8800.Command.Data;

import Net.PC15.Data.INData;
import Net.PC15.Util.ByteUtil;
import Net.PC15.Util.TimeUtil;
import io.netty.buffer.ByteBuf;
import java.util.Calendar;

/**
 * 定时读卡播报语音消息参数
 *
 * @author 赖金杰
 */
public class ReadCardSpeak implements INData {

    @Override
    public int GetDataLen() {
        return 0x0A;
    }

    @Override
    public void SetBytes(ByteBuf data) {
        //data.markReaderIndex();

        Use = data.readBoolean();
        MsgIndex = data.readUnsignedByte();

        byte[] btData = new byte[4];
        data.readBytes(btData, 0, 4);
        BeginDate = TimeUtil.BCDTimeToDate_yyMMddhh(btData);

        data.readBytes(btData, 0, 4);
        EndDate = TimeUtil.BCDTimeToDate_yyMMddhh(btData);
        return;
    }

    @Override
    public ByteBuf GetBytes() {
        ByteBuf buf = ByteUtil.ALLOCATOR.buffer(GetDataLen());
        buf.writeBoolean(Use);
        buf.writeByte(MsgIndex);
        byte[] btData = new byte[4];

        TimeUtil.DateToBCD_yyMMddhh(btData, BeginDate);
        buf.writeBytes(btData);

        TimeUtil.DateToBCD_yyMMddhh(btData, EndDate);
        buf.writeBytes(btData);
        return buf;
    }

    

    /**
     * 启用；0--不启用；1--启用
     */
    public boolean Use;
    /**
     * 消息编号 1--交房租; 2--交管理费;
     */
    public int MsgIndex;
    /**
     * 起始时段 年月日时 BCD码，例：0x11120115，表示11年12月1日15点 最大不得超过2099年
     */
    public Calendar BeginDate;
    /**
     * 结束时段  最大不得超过2099年
     */
    public Calendar EndDate;

}

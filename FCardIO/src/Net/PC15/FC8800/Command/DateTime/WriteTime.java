/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Net.PC15.FC8800.Command.DateTime;

import Net.PC15.Command.CommandParameter;
import Net.PC15.FC8800.Command.FC8800Command;
import Net.PC15.Packet.INPacket;
import Net.PC15.Util.ByteUtil;
import Net.PC15.Util.TimeUtil;
import io.netty.buffer.ByteBuf;
import java.util.Calendar;

/**
 * 将电脑的最新时间写入到控制器中
 *
 * @author 赖金杰
 */
public class WriteTime extends FC8800Command {

    public WriteTime(CommandParameter par) {
        _Parameter = par;
    }

    @Override
    public INPacket GetPacket() {
        byte Datebuf[] = new byte[7];
        //ssmmHHddMMWWyy
        Calendar t = Calendar.getInstance();
        TimeUtil.DateToBCD_ssmmhhddMMwwyy(Datebuf, t);

        ByteBuf dataBuf = ByteUtil.ALLOCATOR.buffer(7);
        dataBuf.writeBytes(Datebuf);

        if (_Packet != null) {
            _Packet.Release();
        }
        _Packet = null;
        
        CreatePacket(2, 2, 0, 7, dataBuf);
        return _Packet;
    }

    @Override
    protected void Release0() {
        return;
    }

}

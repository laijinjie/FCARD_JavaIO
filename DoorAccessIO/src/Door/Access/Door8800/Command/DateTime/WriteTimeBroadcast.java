/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Door.Access.Door8800.Command.DateTime;

import Door.Access.Command.CommandParameter;
import Door.Access.Door8800.Command.Door8800Command;
import Door.Access.Packet.INPacket;
import Door.Access.Util.ByteUtil;
import Door.Access.Util.TimeUtil;
import io.netty.buffer.ByteBuf;
import java.util.Calendar;

/**
 * 校时广播
 *
 * @author 赖金杰
 */
public class WriteTimeBroadcast extends Door8800Command {

    public WriteTimeBroadcast(CommandParameter par) {
        _Parameter = par;
        _IsWaitResponse = true;
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

        CreatePacket(2, 2, 1, 7, dataBuf);
        return _Packet;
    }

    @Override
    protected void Release0() {
        return;
    }

}

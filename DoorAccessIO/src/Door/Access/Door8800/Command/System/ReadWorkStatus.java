/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Door.Access.Door8800.Command.System;

import Door.Access.Command.CommandParameter;
import Door.Access.Connector.INConnectorEvent;
import Door.Access.Door8800.Command.Data.DoorLimit;
import Door.Access.Door8800.Command.Data.DoorPortDetail;
import Door.Access.Door8800.Command.Door8800Command;
import Door.Access.Door8800.Command.System.Result.ReadWorkStatus_Result;
import Door.Access.Door8800.Packet.Door8800PacketModel;
import io.netty.buffer.ByteBuf;

/**
 * 获取控制器各端口工作状态信息.<br>
 * 成功返回结果参考 {@link ReadWorkStatus_Result}
 *
 * @author 赖金杰
 */
public class ReadWorkStatus extends Door8800Command {

    public ReadWorkStatus(CommandParameter par) {
        _Parameter = par;
        CreatePacket(1, 0xE, 0);
    }

    @Override
    protected void Release0() {
        return;
    }

    @Override
    protected boolean _CommandStep(INConnectorEvent oEvent, Door8800PacketModel model) {
        if (CheckResponse_Cmd(model, 1, 0xE, 0, 0x34)) {
            ByteBuf buf = model.GetDatabuff();
            //设定返回值
            ReadWorkStatus_Result ret = new ReadWorkStatus_Result();
            _Result = ret;

            ret.RelayState = GetDoorPortByByteBuf(buf, 4);//门继电器物理状态
            ret.DoorLongOpenState = GetDoorPortByByteBuf(buf, 4);//运行状态   常开还是常闭，0表示常闭，1常开
            ret.DoorState = GetDoorPortByByteBuf(buf, 4);//门磁开关    0表关，1表示开
            ret.DoorAlarmState = GetDoorPortByByteBuf(buf, 4);//门报警状态   
            ret.AlarmState = buf.readUnsignedByte();//设备报警状态
            ret.LockState = GetDoorPortByByteBuf(buf, 8);//继电器逻辑状态
            ret.PortLockState = GetDoorPortByByteBuf(buf, 4);//锁定状态
            ret.WatchState = buf.readByte();//监控状态

            DoorLimit dl = new DoorLimit();

            dl.GlobalEnter = buf.readUnsignedInt();
            for (int i = 0; i < 4; i++) {
                dl.DoorEnter[i] = buf.readUnsignedInt();
            }
            ret.EnterTotal = dl;//门内人数

            ret.TheftState = buf.readByte();//防盗主机布防状态

            RaiseCommandCompleteEvent(oEvent);
            return true;
        } else {
            return false;
        }
    }

    private DoorPortDetail GetDoorPortByByteBuf(ByteBuf buf, int iDoorCount) {
        byte[] bData = new byte[iDoorCount];
        buf.readBytes(bData, 0, iDoorCount);
        DoorPortDetail dt = new DoorPortDetail((short) iDoorCount);
        dt.DoorPort = bData;
        return dt;
    }

}

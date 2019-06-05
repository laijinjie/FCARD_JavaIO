/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Net.PC15.FC8800.Command.System;

import Net.PC15.Command.CommandParameter;
import Net.PC15.Connector.INConnectorEvent;
import Net.PC15.FC8800.Command.Data.BroadcastDetail;
import Net.PC15.FC8800.Command.Data.DoorLimit;
import Net.PC15.FC8800.Command.Data.DoorPortDetail;
import Net.PC15.FC8800.Command.Data.ReadCardSpeak;
import Net.PC15.FC8800.Command.Data.TheftAlarmSetting;
import Net.PC15.FC8800.Command.FC8800Command;
import Net.PC15.FC8800.Command.System.Result.ReadAllSystemSetting_Result;
import Net.PC15.FC8800.Packet.FC8800PacketModel;
import io.netty.buffer.ByteBuf;

/**
 * 获取所有系统参数
 * <p>
 * 成功返回结果参考 {@link ReadAllSystemSetting_Result}
 *
 * @author 赖金杰
 */
public class ReadAllSystemSetting extends FC8800Command {

    public ReadAllSystemSetting(CommandParameter par) {
        _Parameter = par;
        CreatePacket(1, 0xA, 0xFF);
    }

    @Override
    protected void Release0() {
        return;
    }

    @Override
    protected boolean _CommandStep(INConnectorEvent oEvent, FC8800PacketModel model) {
        if (CheckResponse_Cmd(model, 1, 0xA, 0xFF, 0x5C)) {
            ByteBuf buf = model.GetDatabuff();
            //设定返回值
            ReadAllSystemSetting_Result ret = new ReadAllSystemSetting_Result();
            _Result = ret;

            ret.RecordMode = buf.readUnsignedByte();//1      1字节      设置记录存储方式                                   00
            ret.Keyboard = buf.readUnsignedByte();//2      1字节      设置键盘                                           01
            byte btTmp[] = new byte[4];
            buf.readBytes(btTmp, 0, 4);//这4字节已失效

            buf.readBytes(btTmp, 0, 4);
            DoorPortDetail door = new DoorPortDetail((short) 4);
            for (int i = 0; i < 4; i++) {
                door.SetDoor(i + 1, btTmp[i]);
            }
            ret.LockInteraction = door;//4      4字节      互锁参数                                           6,7,8,9
            ret.FireAlarmOption = buf.readUnsignedByte();//5      1字节      消防报警参数                                       10
            ret.OpenAlarmOption = buf.readUnsignedByte();//6      1字节      匪警报警参数                                       11
            ret.ReaderIntervalTime = buf.readUnsignedShort();//7      2字节      读卡间隔时间                                       12,13

            btTmp = new byte[10];
            buf.readBytes(btTmp, 0, 10);
            BroadcastDetail Broadcast = new BroadcastDetail(btTmp);
            ret.SpeakOpen = Broadcast;//8      10字节     语音播报开关                                       14-23
            ret.ReaderCheckMode = buf.readUnsignedByte();//9      1字节      读卡器校验                                         24
            ret.BuzzerMode = buf.readUnsignedByte();//10     1字节      主板蜂鸣器                                         25
            ret.SmogAlarmOption = buf.readUnsignedByte();//11     1字节      烟雾报警参数                                       26

            //门内人数限制
            DoorLimit dl = new DoorLimit();
            dl.GlobalLimit = buf.readUnsignedInt();
            for (int i = 0; i < 4; i++) {
                dl.DoorLimit[i] = buf.readUnsignedInt();
            }
            dl.GlobalEnter = buf.readUnsignedInt();
            for (int i = 0; i < 4; i++) {
                dl.DoorEnter[i] = buf.readUnsignedInt();
            }
            ret.EnterDoorLimit = dl;

            TheftAlarmSetting ts = new TheftAlarmSetting();
            ts.SetBytes(buf);
            ret.TheftAlarmPar = ts;//14     13字节     防盗主机参数                                       67-79
            ret.CheckInOut = buf.readUnsignedByte();//15     1字节      防潜回参数                                         80
            ret.CardPeriodSpeak = buf.readUnsignedByte();//16     1字节      卡片到期提示参数                                   81


            ReadCardSpeak sp =new ReadCardSpeak();
            sp.SetBytes(buf);
            ret.ReadCardSpeak = sp;//17     10字节     定时播报参数                                       82-91

            RaiseCommandCompleteEvent(oEvent);
            return true;
        } else {
            return false;
        }
    }

}

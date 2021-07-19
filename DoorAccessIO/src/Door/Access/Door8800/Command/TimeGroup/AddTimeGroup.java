/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Door.Access.Door8800.Command.TimeGroup;

import Door.Access.Connector.INConnectorEvent;
import Door.Access.Door8800.Command.Card.Result.WriteCardListBySequence_Result;
import Door.Access.Door8800.Command.Data.CardDetail;
import Door.Access.Door8800.Command.Data.TimeGroup.WeekTimeGroup;
import Door.Access.Door8800.Command.Door8800Command;
import Door.Access.Door8800.Command.TimeGroup.Parameter.AddTimeGroup_Parameter;
import Door.Access.Door8800.Packet.Door8800PacketCompile;
import Door.Access.Door8800.Packet.Door8800PacketModel;
import Door.Access.Util.ByteUtil;
import io.netty.buffer.ByteBuf;
import java.util.ArrayList;

/**
 * 设置开门时段
 *
 * @author 徐铭康
 */
public class AddTimeGroup extends Door8800Command {

    protected int mIndex;  //指示当前命令进行的步骤
    protected ArrayList<WeekTimeGroup> List;

    public AddTimeGroup(AddTimeGroup_Parameter par) {

        List = par.List;
        _Parameter = par;

        _ProcessMax = List.size();
        mIndex = 0;
        //初始化缓冲空间
        _CreatePacket();
        //    WriteNext();
    }

    protected void _CreatePacket() {

        ByteBuf dataBuf = ByteUtil.ALLOCATOR.buffer(225);
        CreatePacket(0x06, 0x03, 0, 225, dataBuf);
    }

    @Override
    protected void Release0() {

    }

    private void WriteNext() {
        Door8800PacketCompile compile = (Door8800PacketCompile) _Packet;
        Door8800PacketModel p = (Door8800PacketModel) _Packet.GetPacket();
        ByteBuf dataBuf = p.GetDatabuff();
        dataBuf.clear();
        WeekTimeGroup wtg = List.get(mIndex);
        int groupIndex = wtg.GetIndex();
        if (groupIndex == 0) {
            groupIndex = mIndex + 1;
        }
        if (groupIndex > 64) {
            groupIndex = 1;
        }
        dataBuf.writeByte(groupIndex);
        wtg.GetBytes(dataBuf);
        _ProcessStep = mIndex;
        p.SetDataLen(dataBuf.readableBytes());//重置数据长度
        compile.Compile();//重新编译
        mIndex++;
        CommandReady();
    }

    @Override
    protected boolean _CommandStep(INConnectorEvent oEvent, Door8800PacketModel model) {
        if (CheckResponseOK(model)) {

            CommandNext(oEvent);
            return true;
        }
        return false;
    }

    /**
     * 命令继续执行
     */
    protected void CommandNext(INConnectorEvent oEvent) {
        //增加命令进度
        _ProcessStep = mIndex;
        if (mIndex < List.size() && mIndex < 64) {
            WriteNext();
        } else {
            RaiseCommandCompleteEvent(oEvent);
        }

    }
}

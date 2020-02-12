/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Door.Access.Door8800.Command.Door;

import Door.Access.Connector.INConnectorEvent;
import Door.Access.Door8800.Command.Door.Parameter.DoorPort_Parameter;
import Door.Access.Door8800.Command.Door.Result.ReadReaderWorkSetting_Result;
import Door.Access.Door8800.Command.Door8800Command;
import Door.Access.Door8800.Packet.Door8800PacketModel;
import Door.Access.Util.ByteUtil;
import io.netty.buffer.ByteBuf;

/**
 * 读取控制器单个门的读卡器验证方式参数<br/>
 * 读卡器验证方式是按周时段划分，验证方式分为：只读卡\只密码\读卡加密码\手动输入卡号+密码
 * 成功返回结果参考 {@link ReadReaderWorkSetting_Result}
 *
 * @author 赖金杰
 */
public class ReadReaderWorkSetting extends Door8800Command {

    public ReadReaderWorkSetting(DoorPort_Parameter par) {
        _Parameter = par;
        ByteBuf dataBuf = ByteUtil.ALLOCATOR.buffer(1);
        dataBuf.writeByte(par.Door);
        CreatePacket(3, 5, 0, 1, dataBuf);
    }

    @Override
    protected void Release0() {
        return;
    }

    @Override
    protected boolean _CommandStep(INConnectorEvent oEvent, Door8800PacketModel model) {
        if (CheckResponse_Cmd(model, 3, 5, 0, 0x119)) {
            ByteBuf buf = model.GetDatabuff();

            ReadReaderWorkSetting_Result r = new ReadReaderWorkSetting_Result();
            r.DoorNum = buf.readByte();
            r.ReaderWork.SetBytes(buf);
            
            //设定返回值
            _Result = r;

            RaiseCommandCompleteEvent(oEvent);
            return true;
        } else {
            return false;
        }
    }

}

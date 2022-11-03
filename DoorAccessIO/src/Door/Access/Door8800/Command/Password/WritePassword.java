/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Door.Access.Door8800.Command.Password;

import Door.Access.Connector.INConnectorEvent;
import Door.Access.Door8800.Command.Data.PasswordDetail;
import Door.Access.Door8800.Command.Door8800Command;
import Door.Access.Door8800.Command.Password.Parameter.WritePassword_Parameter;
import Door.Access.Door8800.Command.Password.Result.WritePassword_Result;
import Door.Access.Door8800.Packet.Door8800PacketCompile;
import Door.Access.Door8800.Packet.Door8800PacketModel;
import Door.Access.Util.ByteUtil;
import io.netty.buffer.ByteBuf;

import java.util.ArrayList;

/**
 * 写入开门密码
 *
 * @author F
 */
public class WritePassword extends Door8800Command {

    //  int PasswordSize;
    protected WritePassword_Parameter par;

    protected int UploadMax;
    protected int Index = 0;

    protected int Page = 65;

    protected int Step = 0;

    public WritePassword(WritePassword_Parameter par) {
        _Parameter = par;
        this.par = par;
        UploadMax = par.passwordDetails.size();
        _ProcessMax = UploadMax;
        CreatePacket(0x05, 0x01);
    }

    protected void CheckWritePasswordResponse(INConnectorEvent oEvent, Door8800PacketModel model) {
        if (CheckResponseOK(model)) {

            if (Index < UploadMax) {
                WritePassword();
            } else {
                RaiseCommandCompleteEvent(oEvent);
            }
        } else if (CheckResponse_Cmd(model, 0x05, 0x04, 0xff)) {
            ByteBuf buf = model.GetDatabuff();
            WritePassword_Result result = new WritePassword_Result();
            result.ErrorCount = buf.readInt();
            result.ErrorDetails = new ArrayList<>();
            for (int i = 0; i < result.ErrorCount; i++) {
                PasswordDetail detail = new PasswordDetail();
                detail.SetBytes(buf);
                result.ErrorDetails.add(detail);
            }
            _Result = result;
            RaiseCommandCompleteEvent(oEvent);
        }
    }

    private void WritePassword() {
        if (Page > UploadMax) {
            Page = UploadMax;
        }
        Door8800PacketCompile compile = (Door8800PacketCompile) _Packet;
        Door8800PacketModel p = (Door8800PacketModel) _Packet.GetPacket();
        ByteBuf dataBuf = GetBytes();
        p.SetDataLen(dataBuf.readableBytes());//重置数据长度
        p.SetCmdType((short) 0x05);
        p.SetCmdIndex((short) 0x04);
        p.SetCmdPar((short) 0x00);
        p.SetDatabuff(dataBuf);
        compile.Compile();//重新编译
        Index = Index + Page;
        _ProcessStep = Index;
        CommandReady();
    }

    protected ByteBuf GetBytes() {
        int size = 0x04 + (5 * Page);
        ByteBuf dataBuf = ByteUtil.ALLOCATOR.buffer(size);
        dataBuf.writeInt(Page);
        int maxCount = Index + Page;
        if (maxCount > UploadMax) {
            maxCount = UploadMax;
        }
        for (int i = Index; i < UploadMax; i++) {
            PasswordDetail passwordDetail = (PasswordDetail) par.passwordDetails.get(i);
            passwordDetail.GetBytes(dataBuf);
            if (maxCount == (i + 1)) {
                break;
            }
        }
        return dataBuf;
    }

    private void CheckReadPasswordDataBaseResponse(INConnectorEvent oEvent, Door8800PacketModel model) {
        if (CheckResponse_Cmd(model, 0x05, 0x01)) {
            ByteBuf buf = model.GetDatabuff();
            int Capacity = buf.readUnsignedShort();
            int UseNumber = buf.readUnsignedShort();
            int count = Capacity - UseNumber;
            if (UploadMax > count) {
                WritePassword_Result result = new WritePassword_Result();
                result.OverflowCount = UploadMax - count;
                _Result = result;
                RaiseCommandCompleteEvent(oEvent);
            } else {
                Step = 1;
                _ProcessStep = 0;
                WritePassword();
            }
        }
    }

    @Override
    protected boolean _CommandStep(INConnectorEvent oEvent, Door8800PacketModel model) {
        switch (Step) {
            case 0:
                CheckReadPasswordDataBaseResponse(oEvent, model);
                break;
            case 1:
                CheckWritePasswordResponse(oEvent, model);
                break;
            default:
                break;
        }
        return true;
    }

    @Override
    protected void Release0() {

    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Door.Access.Door8800.Command;


import Door.Access.Command.AbstractCommand;
import Door.Access.Command.E_CommandStatus;
import Door.Access.Connector.INConnectorEvent;
import Door.Access.Door8800.Door8800Identity;
import Door.Access.Door8800.Packet.*;
import Door.Access.Packet.INPacketModel;
import Door.Access.Util.StringUtil;
import io.netty.buffer.ByteBuf;

/**
 * Door8800的命令处理类
 *
 * @author 赖金杰
 */
public abstract class Door8800Command extends AbstractCommand {

    public static String NULLPassword = "FFFFFFFF";
    /**
     * 此型号控制器最大门数
     */
    public static final short DoorMax = 4;

    public Door8800Command() {
        _Decompile = new Door8800Decompile();
    }

    protected void CreatePacket(int iCmdType, int iCmdIndex) {
        CreatePacket(iCmdType, iCmdIndex, 0, 0, null);
    }

    protected void CreatePacket(int iCmdType, int iCmdIndex, int iCmdPar) {
        CreatePacket(iCmdType, iCmdIndex, iCmdPar, 0, null);;
    }

    protected void CreatePacket(int iCmdType, int iCmdIndex, int iCmdPar, int iDataLen, ByteBuf Databuf) {
        Door8800Identity identity = (Door8800Identity) _Parameter.getCommandDetail().Identity;
        String sn = identity.GetSN();
        String pwd = identity.GetPassword();
        sn = StringUtil.FillString(sn, 16, "0");
        pwd = StringUtil.FillString(pwd, 8, "F");
        long lPassword = Long.parseLong(pwd, 16);

        if (_Packet != null) {
            _Packet.Release();
        }
        _Packet = new Door8800PacketCompile(sn, lPassword, (short) iCmdType, (short) iCmdIndex, (short) iCmdPar, iDataLen, Databuf);
        CommandReady();
    }

    @Override
    protected boolean CommandStep(INConnectorEvent oEvent, INPacketModel model) {
        Door8800PacketModel pmodel = (Door8800PacketModel) model;

        Door8800PacketModel SendModel = (Door8800PacketModel) _Packet.GetPacket();
        if (pmodel.GetCode() != SendModel.GetCode()) {
            return false; //检查命令标志是否一致
        }

        if (CheckResponse_PasswordErr(pmodel)) {
            CommandOver();//发生错误
            RaisePasswordErrorEvent(oEvent);
            return false;
        }

        if (CheckResponse_CheckSumErr(pmodel)) {
            _Status = E_CommandStatus.OnReady; //重新发送
            if (_SendCount > 100) {
                CommandOver();//发生错误
                RaiseChecksumErrorEvent(oEvent);
            }
            return false;
        }
        return _CommandStep(oEvent, pmodel);
    }

    /**
     * 进行命令的结果的检查，默认为检查OK，不是此方法的需要重写
     *
     * @param oEvent 推进命令的进程。
     * @param model
     * @return
     */
    protected boolean _CommandStep(INConnectorEvent oEvent, Door8800PacketModel model) {
        if (CheckResponseOK(model)) {
            RaiseCommandCompleteEvent(oEvent);
            return true;
        } else {
            return false;
        }
    }

    /**
     * OK 指令
     *
     * @param model
     * @return
     */
    protected boolean CheckResponseOK(Door8800PacketModel model) {
        return (model.GetCmdType() == 0x21 && model.GetCmdIndex() == 0x01);
    }

    /**
     * 通讯密码错误
     *
     * @param model
     * @return
     */
    protected boolean CheckResponse_PasswordErr(Door8800PacketModel model) {
        return (model.GetCmdType() == 0x21 && model.GetCmdIndex() == 0x02);
    }

    /**
     * 检验和错误
     *
     * @param model
     * @return
     */
    protected boolean CheckResponse_CheckSumErr(Door8800PacketModel model) {
        return (model.GetCmdType() == 0x21 && model.GetCmdIndex() == 0x03);
    }

    protected boolean CheckResponse_Cmd(Door8800PacketModel model, int CmdType, int CmdIndex) {
        return (model.GetCmdType() == (0x30 + CmdType) && model.GetCmdIndex() == CmdIndex);
    }

    protected boolean CheckResponse_Cmd(Door8800PacketModel model, int CmdType, int CmdIndex, int CmdPar) {
        return (model.GetCmdType() == (0x30 + CmdType) && model.GetCmdIndex() == CmdIndex && model.GetCmdPar() == CmdPar);
    }

    protected boolean CheckResponse_Cmd(Door8800PacketModel model, int CmdType, int CmdIndex, int CmdPar, int DataLen) {
        return (model.GetCmdType() == (0x30 + CmdType) && model.GetCmdIndex() == CmdIndex && model.GetCmdPar() == CmdPar && model.GetDataLen() == DataLen);
    }
}

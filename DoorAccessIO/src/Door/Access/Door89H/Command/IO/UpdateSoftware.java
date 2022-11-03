/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Door.Access.Door89H.Command.IO;

import Door.Access.Connector.INConnectorEvent;
import Door.Access.Door8800.Command.Door8800Command;
import Door.Access.Door8800.Packet.Door8800PacketCompile;
import Door.Access.Door8800.Packet.Door8800PacketModel;
import Door.Access.Door89H.Command.IO.Parameter.UpdateSoftware_Parameter;
import Door.Access.Door89H.Command.IO.Result.UpdateSoftware_Result;
import Door.Access.Util.ByteUtil;
import io.netty.buffer.ByteBuf;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author FCARD
 */
public class UpdateSoftware extends Door8800Command {

    int Step = 0;
    UpdateSoftware_Parameter parameter;

    int WriteIndex = 0;

    public UpdateSoftware(UpdateSoftware_Parameter parameter) {
        _Parameter = parameter;
        this.parameter = parameter;
        int size = 4;
        ByteBuf buf = ByteUtil.ALLOCATOR.buffer(size);
        buf.writeInt(parameter.getSoftwareSize());
        CreatePacket(0x0A, 0x01, 0x00, 4, buf);//准备写入升级
    }

    @Override
    protected boolean _CommandStep(INConnectorEvent oEvent, Door8800PacketModel model) {
        // System.out.println(WriteIndex);
        //  CommandWaitResponse();
        switch (Step) {
            case 0:
                if (CheckResponseOK(model)) {
                    try {
                        CheckOKResult();
                    } catch (IOException ex) {
                        Logger.getLogger(UpdateSoftware.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                return true;
            case 1: {
                try {
                    CheckWriteFileResult(oEvent, model);
                } catch (IOException ex) {
                    Logger.getLogger(UpdateSoftware.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            return true;
            case 2:
                if (CheckResponse_Cmd(model, 0x0A, 0x3, 0x00, 1)) { //crc校验成功
                    ByteBuf buf = model.GetDatabuff();
                    _Result = new UpdateSoftware_Result(buf.readByte());
                    RaiseCommandCompleteEvent(oEvent);
                   System.out.println("发送完后长度："+parameter.getSoftwareSize());
                }
                return true;
            default:
                break;
        }
        return false;
    }

    /**
     * *
     * 准备写入响应OK
     *
     * @param oEvent
     * @param model
     */
    private void CheckOKResult() throws IOException {
        int iPackSize = 256;
        byte[] data = parameter.getSoftware();
        if (iPackSize > data.length) {
            iPackSize = data.length;
        }
        _ProcessMax = data.length;
        _ProcessStep = 0;
        int iBufSize = 3 + iPackSize;
        ByteBuf writeBuf = ByteUtil.ALLOCATOR.buffer(iBufSize);
        WriteIndex = 0;
        writeBuf.writeMedium(WriteIndex);
        writeBuf.writeBytes(data, 0, iPackSize);
        CreatePacket(0x0A, 0x2, 0, writeBuf.readableBytes(), writeBuf);
     //   WriteFiel(data, 0, iPackSize, false);
        Step = 1;
        CommandReady();
    }

    private void CheckWriteFileResult(INConnectorEvent oEvent, Door8800PacketModel model) throws IOException {
        //持续发送
        if (CheckResponse_Cmd(model, 0x0A, 0x2, 0)) {
            byte[] data = parameter.getSoftware();
            int iPackSize = 256;

            WriteIndex += iPackSize;
            _ProcessStep += iPackSize;
            int iFileLen = parameter.getSoftwareSize();
            int iDataLen = iFileLen - WriteIndex;

            Door8800PacketCompile compile = (Door8800PacketCompile) _Packet;
            Door8800PacketModel p = (Door8800PacketModel) _Packet.GetPacket();
            ByteBuf buf = p.GetDatabuff();
            buf.clear();
            if (iDataLen > iPackSize) {
                iDataLen = iPackSize;
            }
            if (iDataLen <= 0) {
                //写入文件crc校验
                _ProcessStep = _ProcessMax;
                int crc32 = (int) parameter.getCrc32();
                buf.writeInt(crc32);
                // CreatePacket(0x0A, 0x3, 0, buf.readableBytes(), buf);
                Step = 2;
                p.SetCmdIndex((short) 0x3);
            } else {
                //写入文件
                buf.writeMedium(WriteIndex);
                buf.writeBytes(data, WriteIndex, iDataLen);
                p.SetCmdIndex((short) 0x2);
               // WriteFiel(data, WriteIndex, iDataLen, true);
              //  System.out.println(iDataLen);
            }
            p.SetCmdPar((short) 0);
            p.SetDataLen(buf.readableBytes());//重置数据长度
            compile.Compile();//重新编译
            CommandReady();

        } else if (CheckResponse_Cmd(model, 0x0A, 0x2, 2))//发送完成
        {
            _Result = new UpdateSoftware_Result((byte) 255);
            RaiseCommandCompleteEvent(oEvent);
        }
    }

    private void WriteFiel(byte [] buf,int off,int len, boolean isApped) throws IOException {
        BufferedOutputStream fw = new BufferedOutputStream(new FileOutputStream("D:\\E\\应用软件包\\111.RCBin", isApped));
        fw.write(buf,off,len);
        fw.close();
    }

    @Override
    protected void Release0() {

    }

}

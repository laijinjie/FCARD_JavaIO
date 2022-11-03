/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Face.System;

import Door.Access.Command.CommandParameter;
import Door.Access.Connector.INConnectorEvent;
import Door.Access.Door8800.Command.Door8800Command;
import Door.Access.Door8800.Packet.Door8800PacketModel;
import Face.System.Result.ReadVersion_Result;
import io.netty.buffer.ByteBuf;

/**
 * 读取版本号
 *
 * @author F
 */
public class ReadVersion extends Door8800Command {
    /**
     * 读取版本号
     *
     * @param par 命令参数
     */
    public ReadVersion(CommandParameter par) {
        _Parameter = par;
        CreatePacket(1, 8);
    }

    @Override
    protected void Release0() {
        return;
    }

    @Override
    protected boolean _CommandStep(INConnectorEvent oEvent, Door8800PacketModel model) {
        if (CheckResponse_Cmd(model, 0x01, 0x08)) {
            ByteBuf buf = model.GetDatabuff();

            ReadVersion_Result result = new ReadVersion_Result();
            _Result = result;
            byte[] bVersion = new byte[4];
            byte[] bFingerprintVersion = new byte[4];
            byte[] bFaceVersion = new byte[3];
            try {
                buf.readBytes(bVersion);
                buf.readBytes(bFingerprintVersion);
                buf.readBytes(bFaceVersion);
                result.Version = new String(bVersion, "ASCII");
                result.FingerprintVersion = new String(bFingerprintVersion, "ASCII");
                result.FaceVersion = new String(bFaceVersion, "ASCII");
                StringBuilder sb = new StringBuilder(result.Version);
                sb.insert(2, ".");
                result.Version = sb.toString();
                sb = new StringBuilder(result.FingerprintVersion);
                sb.insert(2, ".");
                result.FingerprintVersion = sb.toString();
                sb = new StringBuilder(result.FaceVersion);
                sb.insert(1, ".");
                result.FaceVersion = sb.toString();

            } catch (Exception e) {
                result.Version = null;
                result.FingerprintVersion = null;
                result.FaceVersion = null;
            }
            RaiseCommandCompleteEvent(oEvent);
            return true;
        } else {
            return false;
        }
    }
}

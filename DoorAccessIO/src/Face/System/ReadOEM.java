package Face.System;

import Door.Access.Command.CommandParameter;
import Door.Access.Connector.INConnectorEvent;
import Door.Access.Door8800.Command.Door8800Command;
import Door.Access.Door8800.Packet.Door8800PacketModel;
import Face.Data.OEMDetail;
import Face.System.Result.OEM_Result;

import java.io.UnsupportedEncodingException;

/**
 * 读取OEM信息
 */
public class ReadOEM extends Door8800Command {
    /**
     * 读取OEM信息
     * @param parameter
     */
    public ReadOEM(CommandParameter parameter) {
        _Parameter = parameter;
        CreatePacket(0x01, 0x1e, 0x01);
    }

    @Override
    protected boolean _CommandStep(INConnectorEvent oEvent, Door8800PacketModel model) {
        if (CheckResponse_Cmd(model, 127)) {

            OEMDetail detail = new OEMDetail();
            try {
                detail.setBytes(model.GetDatabuff());
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            OEM_Result result = new OEM_Result(detail);
            _Result = result;
            RaiseCommandCompleteEvent(oEvent);
            return  true;
        }

        return false;
    }

    @Override
    protected void Release0() {

    }
}

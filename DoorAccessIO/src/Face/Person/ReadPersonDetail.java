package Face.Person;

import Door.Access.Connector.INConnectorEvent;
import Door.Access.Door8800.Command.Door8800Command;
import Door.Access.Door8800.Packet.Door8800PacketModel;
import Door.Access.Util.ByteUtil;
import Face.Data.Person;
import Face.Person.Parameter.ReadPersonDetail_Parameter;
import Face.Person.Result.ReadPersonDetail_Result;
import io.netty.buffer.ByteBuf;

import java.io.UnsupportedEncodingException;

/**
 * 查询人员数据详情
 *
 * @author F
 */
public class ReadPersonDetail extends Door8800Command {

    /**
     * 查询人员数据详情
     *
     * @param parameter 查询参数
     */
    public ReadPersonDetail(ReadPersonDetail_Parameter parameter) {
        _Parameter = parameter;
        ByteBuf buf = ByteUtil.ALLOCATOR.buffer(4);
        buf.writeInt((int) parameter.UserCode);
        CreatePacket(0x07, 0x03, 0x01, 4, buf);
    }

    @Override
    protected boolean _CommandStep(INConnectorEvent oEvent, Door8800PacketModel model) {
        boolean bResult = false;
        if (CheckResponse_Cmd(model, 0xA1)) {
            ByteBuf buf = model.GetDatabuff();
            ReadPersonDetail_Result result = new ReadPersonDetail_Result();
            result.isReady = buf.getByte(0) != 0xff;
            if (result.isReady) {
                try {
                    result.person = new Person();
                    result.person.setBytes(buf);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
            bResult = true;
            _Result = result;
        }
        RaiseCommandCompleteEvent(oEvent);
        return bResult;
    }

    @Override
    protected void Release0() {

    }
}

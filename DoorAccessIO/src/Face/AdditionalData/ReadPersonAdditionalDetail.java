package Face.AdditionalData;

import Door.Access.Connector.INConnectorEvent;
import Door.Access.Door8800.Command.Door8800Command;
import Door.Access.Door8800.Packet.Door8800PacketModel;
import Door.Access.Util.ByteUtil;
import Face.AdditionalData.Parameter.ReadPersonAdditionalDetail_Parameter;
import Face.AdditionalData.Result.ReadPersonAdditionalDetail_Result;
import io.netty.buffer.ByteBuf;

public class ReadPersonAdditionalDetail extends Door8800Command {
    /**
     *查询人员附加数据详情
     */
    public  ReadPersonAdditionalDetail(ReadPersonAdditionalDetail_Parameter parameter){
        _Parameter =parameter;
        ByteBuf buf= ByteUtil.ALLOCATOR.buffer(4);
        buf.writeInt(parameter.UserCode);
        CreatePacket(0x0B, 0x04, 0x00,4,buf);
    }
    @Override
    protected boolean _CommandStep(INConnectorEvent oEvent, Door8800PacketModel model) {
        boolean bResult=false;
        if(CheckResponse_Cmd(model,20)){
            ReadPersonAdditionalDetail_Result result = new ReadPersonAdditionalDetail_Result();
            result.setBytes(model.GetDatabuff());
            bResult=true;
            _Result=result;
        }
        RaiseCommandCompleteEvent(oEvent);
        return  bResult;
    }
    @Override
    protected void Release0() {

    }
}

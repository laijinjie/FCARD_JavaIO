package Face.Person;

import Door.Access.Connector.INConnectorEvent;
import Door.Access.Door8800.Command.Door8800Command;
import Door.Access.Door8800.Packet.Door8800PacketModel;
import Door.Access.Util.ByteUtil;
import Face.Person.Parameter.DeletePerson_Parameter;
import io.netty.buffer.ByteBuf;

import java.util.ArrayList;

/**
 * 删除人员
 *
 * @author F
 */
public class DeletePerson extends Door8800Command {

    int mStep, mWriteIndex = 0, mPacketMax;
    ArrayList<Long> UserCodeList;
    int ListSize;

    /**
     * 删除人员
     *
     * @param parameter 删除人员参数
     */
    public DeletePerson(DeletePerson_Parameter parameter) {
        _Parameter = parameter;
        UserCodeList = parameter.UserCodeList;
        ListSize = UserCodeList.size();
        mPacketMax = 100;
        //    MaxBufSize = (20 * 4) + 1;
        CommandNext();
    }

    private void CommandNext() {
        int iCount = ListSize - mWriteIndex;
        if (iCount > mPacketMax) {
            iCount = mPacketMax;
        }
        int bufSize = (iCount * 4) + 1;
        ByteBuf buf = ByteUtil.ALLOCATOR.buffer(bufSize);
        buf.writeByte(iCount);
        int len=iCount+mWriteIndex;
        for (int i = mWriteIndex; i < len; i++) {
            long UserCode=UserCodeList.get(mWriteIndex);
            buf.writeInt((int)UserCode);
            mWriteIndex++;
        }
        CreatePacket(0x07, 0x05, 0x00, bufSize, buf);
    }

//    private ByteBuf GetBufData(ByteBuf buf) {
//        int iCount = ListSize - mWriteIndex;
//        if (iCount > mPacketMax) {
//            iCount = mPacketMax;
//        }
//        buf.writeByte(iCount);
//        for (int i = 0; i < iCount; i++) {
//            buf.writeInt(UserCodeList.get(mWriteIndex));
//            mWriteIndex++;
//        }
//        return buf;
//    }

    @Override
    protected boolean _CommandStep(INConnectorEvent oEvent, Door8800PacketModel model) {
        if (CheckResponseOK(model)) {
            // RaiseCommandCompleteEvent(oEvent);
            if (IsWriteOver()) {
                RaiseCommandCompleteEvent(oEvent);
            } else {
                CommandNext();
                CommandWaitResponse();
            }
            return true;
        }
        return false;
    }

    private boolean IsWriteOver() {
        return ListSize <= mWriteIndex;
    }

    @Override
    protected void Release0() {

    }
}

package Face.Person;

import Door.Access.Connector.INConnectorEvent;
import Door.Access.Door8800.Command.Door8800Command;
import Door.Access.Door8800.Packet.Door8800PacketModel;
import Door.Access.Util.ByteUtil;
import Face.Data.Person;
import Face.Person.Parameter.Person_Parameter;
import Face.Person.Result.Person_Result;
import io.netty.buffer.ByteBuf;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * 添加人员
 *
 * @author F
 */
public class AddPerson extends Door8800Command {

    int mStep, mWriteIndex = 0, mPacketMax, MaxBufSize = 350;
    ArrayList<Person> PersonList;
    int ListSize;
    /**
     * 读取到的人员数据缓冲
     */
    private ConcurrentLinkedQueue<ByteBuf> mBufs;

    /**
     * 添加人员
     *
     * @param parameter 添加人员参数
     */
    public AddPerson(Person_Parameter parameter) {
        _Parameter = parameter;
        mPacketMax = 1;
        MaxBufSize = mPacketMax * 0xA1 + 1;
        PersonList = parameter.PersonList;
        ListSize = parameter.PersonList.size();
        mBufs = new ConcurrentLinkedQueue<ByteBuf>();
        CommandNext();
    }

    private void CommandNext() {
        ByteBuf buf = ByteUtil.ALLOCATOR.buffer(MaxBufSize);
        try {
            CreatePacket(0x07, 0x04, 0x00, MaxBufSize, GetBufData(buf));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    private ByteBuf GetBufData(ByteBuf buf) throws UnsupportedEncodingException {
        buf.writeByte(1);
        Person person = PersonList.get(mWriteIndex);
        person.getBytes(buf);
        return buf;
    }

    @Override
    protected boolean _CommandStep(INConnectorEvent oEvent, Door8800PacketModel model) {

        if (CheckResponseOK(model)) {
            if (IsWriteOver()) {
                setErrorData();
                RaiseCommandCompleteEvent(oEvent);
            } else {
                mWriteIndex++;
                CommandNext();
                RaiseCommandProcessEvent(oEvent);
                CommandWaitResponse();
            }
            return true;
        }

        if (CheckResponse_Cmd(model, 0x07, 0x04, 0xFF)) {
            mBufs.add(model.GetDatabuff());
            if (IsWriteOver()) {
                setErrorData();
                RaiseCommandCompleteEvent(oEvent);
                return true;
            } else {
                mWriteIndex++;
                CommandNext();
                RaiseCommandProcessEvent(oEvent);
                CommandWaitResponse();
            }
            return true;
        }

        return false;
    }

    private void setErrorData() {
        ArrayList<Integer> list = new ArrayList<Integer>();
        while (mBufs.peek() != null) {
            ByteBuf buf = mBufs.poll();
            int iCount = buf.readInt();
            for (int i = 0; i < iCount; i++) {
                list.add((int) buf.readUnsignedInt());
            }
        }
        _Result = new Person_Result(list);
    }

    private boolean IsWriteOver() {
        return ((ListSize - 1) - mWriteIndex) == 0;
    }

    @Override
    protected void Release0() {

    }
}

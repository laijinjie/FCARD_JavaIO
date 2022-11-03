package Face.Person;

import Door.Access.Command.CommandParameter;
import Door.Access.Connector.INConnectorEvent;
import Door.Access.Door8800.Command.Door8800Command;
import Door.Access.Door8800.Packet.Door8800PacketModel;
import Door.Access.Util.ByteUtil;
import Face.Data.Person;
import Face.Person.Result.PersonDataBase_Result;
import io.netty.buffer.ByteBuf;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * 从设备中读取所有已注册的人员信息
 *
 * @author F
 */
public class ReadPersonDataBase extends Door8800Command {
    /**
     * 指示当前命令进行的步骤
     */
    private int mStep;
    /**
     * 读索引
     */
    private int mReadIndex;
    /**
     * 每次读取数量
     */
    private int mReadCount;
    /**
     * 用户总数
     */
    private int mUserTotal;
    /**
     * 读取到的人员数据缓冲
     */
    private ConcurrentLinkedQueue<ByteBuf> mBufs;

    /**
     * 从设备中读取所有已注册的人员信息
     *
     * @param parameter 命令参数
     */
    public ReadPersonDataBase(CommandParameter parameter) {
        _Parameter = parameter;
        CreatePacket(0x07, 0x01);
        mStep = 1;
    }

    @Override
    protected boolean _CommandStep(INConnectorEvent oEvent, Door8800PacketModel model) {
        switch (mStep) {
            case 1:
                if (CheckResponse_Cmd(model, 0x07, 0x01)) {
                    return ReadDetailCallBlack(oEvent, model);
                }
                break;
            case 2:
                if (CheckResponse_Cmd(model, 0x07, 0x13)) {
                    return ReadPersonDatabaseCallBlack(oEvent, model);
                }

                break;
            default:
                break;
        }
        return false;
    }

    /**
     * 读取人员数据库内容
     *
     * @param oEvent
     * @param model
     * @return
     */
    private boolean ReadPersonDatabaseCallBlack(INConnectorEvent oEvent, Door8800PacketModel model) {
        try {

            ByteBuf buf = model.GetDatabuff();
            int iCount = buf.getByte(0);
            _ProcessStep += iCount;
            /**
             * 打印进度
             */
            RaiseCommandProcessEvent(oEvent);
            /**
             * 保留数据
             */
            buf.retain();
            mBufs.add(buf);
            mReadIndex += mReadCount;
            if (mReadIndex == mUserTotal) {
                return ReadPersonDatabaseOverCallBlack(oEvent, model);
            } else {
                ReadDetailNext();
            }
            return true;
        } catch (Exception ex) {
            return false;
        }

    }

    /**
     * 读取人员数据库内容完毕
     *
     * @param oEvent
     * @param model
     */
    private boolean ReadPersonDatabaseOverCallBlack(INConnectorEvent oEvent, Door8800PacketModel model) throws UnsupportedEncodingException {
        int iCount = 0;
        ByteBuf buf;
        ArrayList<Person> persons = new ArrayList<Person>();
        while (mBufs.peek() != null) {
            buf = mBufs.poll();
            iCount = buf.readByte();
            for (int i = 0; i < iCount; i++) {
                Person person = new Person();
                person.setBytes(buf);
                persons.add(person);
            }

        }
        PersonDataBase_Result result = new PersonDataBase_Result(persons);
        _Result = result;
        RaiseCommandCompleteEvent(oEvent);
        return true;
    }

    /**
     * 读取人员数据库详情回调
     *
     * @param oEvent
     * @param model
     * @return
     */
    private boolean ReadDetailCallBlack(INConnectorEvent oEvent, Door8800PacketModel model) {
        mUserTotal = model.GetDatabuff().getInt(4);
        if (mUserTotal == 0) {

        }
        _ProcessMax = mUserTotal;
        mBufs = new ConcurrentLinkedQueue<ByteBuf>();
        mStep = 2;
        mReadCount = 5;
        mReadIndex = 0;
        ReadDetailNext();
        CommandWaitResponse();
        return true;
    }

    /**
     * 继续读取人事档案
     */
    public void ReadDetailNext() {
        int iNewCount = mUserTotal - mReadIndex;
        if (mReadCount > iNewCount) {
            mReadCount = iNewCount;
        }
        ByteBuf buf = ByteUtil.ALLOCATOR.buffer(5);
        buf.writeInt(mReadIndex);
        buf.writeByte(mReadCount);
        CreatePacket(0x07, 0x013, 0x00, 5, buf);
    }

    @Override
    protected void Release0() {

    }
}

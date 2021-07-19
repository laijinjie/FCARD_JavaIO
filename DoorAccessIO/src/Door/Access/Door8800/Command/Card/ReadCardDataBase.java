/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Door.Access.Door8800.Command.Card;

import Door.Access.Connector.INConnectorEvent;
import Door.Access.Door8800.Command.Card.Parameter.ReadCardDataBase_Parameter;
import Door.Access.Door8800.Command.Card.Result.ReadCardDataBase_Result;
import Door.Access.Door8800.Command.Card.Result.ReadCardDatabaseDetail_Result;
import Door.Access.Door8800.Command.Data.CardDetail;
import Door.Access.Door8800.Command.Door8800Command;
import Door.Access.Door8800.Packet.Door8800PacketModel;
import Door.Access.Util.ByteUtil;
import io.netty.buffer.ByteBuf;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * 从控制器中读取卡片数据<br>
 * 成功返回结果参考 {@link ReadCardDataBase_Result}
 *
 * @author 赖金杰
 */
public class ReadCardDataBase extends Door8800Command {

    protected int mStep;//指示当前命令进行的步骤
    protected ConcurrentLinkedQueue<ByteBuf> mBufs;
    protected int mRecordCardSize;//记录的卡数量

    public ReadCardDataBase() {

    }

    public ReadCardDataBase(ReadCardDataBase_Parameter par) {
        _Parameter = par;
        _ProcessMax = 2;
        _ProcessStep = 1;

        mStep = 1;//第一步，读取卡片存储信息，并分析出是否已存储卡片
        CreatePacket(7, 1);

    }

    @Override
    protected void Release0() {
        ClearBuf();
        mBufs = null;
        _Parameter = null;
        _Result = null;
        return;
    }

    @Override
    protected boolean _CommandStep(INConnectorEvent oEvent, Door8800PacketModel model) {
        switch (mStep) {
            case 1:
                return CheckDataBaseDetail(oEvent, model);
            case 2:
                return CheckReadCardPacket(oEvent, model);
        }
        return false;

    }

    /**
     * 检查卡片数据库的信息，是否有卡可读取
     *
     * @param oEvent 事件句柄
     * @param model 本次数据包的包装类
     * @return true 正确解析或 false 未解析
     */
    protected boolean CheckDataBaseDetail(INConnectorEvent oEvent, Door8800PacketModel model) {
        if (CheckResponse_Cmd(model, 7, 1, 0, 0x10)) {
            ByteBuf buf = model.GetDatabuff();

            ReadCardDatabaseDetail_Result r = new ReadCardDatabaseDetail_Result();
            r.SortDataBaseSize = buf.readUnsignedInt();
            r.SortCardSize = buf.readUnsignedInt();
            r.SequenceDataBaseSize = buf.readUnsignedInt();
            r.SequenceCardSize = buf.readUnsignedInt();

            ReadCardDataBase_Parameter par = (ReadCardDataBase_Parameter) _Parameter;
            ReadCardDataBase_Result result = new ReadCardDataBase_Result(par.CardType);
            _Result = result;
            mRecordCardSize = 0;
            switch (par.CardType) {
                case 1://排序卡区域
                    if (r.SortCardSize > 0) {
                        mRecordCardSize = (int) r.SortCardSize;
                    }
                    break;
                case 2://非排序卡区域
                    if (r.SequenceCardSize > 0) {
                        mRecordCardSize = (int) r.SequenceCardSize;
                    }

                    break;
                case 3://所有区域
                    if ((r.SortCardSize + r.SequenceCardSize) > 0) {
                        mRecordCardSize = (int) (r.SortCardSize + r.SequenceCardSize);
                    }
                    break;
            }
            if (mRecordCardSize > 0) {
                _ProcessMax = mRecordCardSize;
                _ProcessStep = 0;
                //发生命令进度变更
                //RaiseCommandProcessEvent(oEvent);
                //发送读取所有卡的指令
                buf = ByteUtil.ALLOCATOR.buffer(1);
                buf.writeByte(par.CardType);
                CreatePacket(7, 3, 0, 1, buf);

                CommandWaitResponse();

                mBufs = new ConcurrentLinkedQueue<ByteBuf>();
                mStep = 2;

            } else {
                RaiseCommandCompleteEvent(oEvent);
            }

        } else {
            return false;
        }
        return true;
    }

    /**
     * 检查读所有卡命令包
     *
     * @param oEvent 事件句柄
     * @param model 本次数据包的包装类
     * @return true 正确解析或 false 未解析
     */
    protected boolean CheckReadCardPacket(INConnectorEvent oEvent, Door8800PacketModel model) {
        if (CheckResponse_Cmd(model, 7, 3, 0)) {
            //System.out.println(" 已收到数据：\n" + ByteBufUtil.hexDump(msg));
            try {
                ByteBuf buf = model.GetDatabuff();
                int iCardSize = buf.getInt(0);
                _ProcessStep += iCardSize;
                RaiseCommandProcessEvent(oEvent);

                buf.retain();
                mBufs.add(buf);

                //让命令持续等待下去
                CommandWaitResponse();
                //System.out.println("接收数据： _SendDate = " + TimeUtil.FormatTimeMillisecond(_SendDate));
            } catch (Exception e) {
                System.out.println("发送错误：" + e.getLocalizedMessage());
            }

            return true;

        } else if (CheckResponse_Cmd(model, 7, 3, 0xFF, 4)) {
            ByteBuf buf = model.GetDatabuff();
            int iCardSize = buf.readInt();
            /*
            if (iCardSize == 0) {
                buf = mBufs.poll();
                iCardSize = (buf.capacity() - 4) / 33;
            }
             */
            if (iCardSize > 0 || (!mBufs.isEmpty())) {
                //Calendar begintime = Calendar.getInstance();
                //开始拆分接收到的数据包
                try {
                    Analysis(iCardSize);
                } catch (Exception e) {

                }
                //Calendar endtime = Calendar.getInstance();
                //long waitTime = endtime.getTimeInMillis() - begintime.getTimeInMillis();
                //System.out.println("解析数据包耗时：" + waitTime);

                //拆分后返回事件
                RaiseCommandCompleteEvent(oEvent);
            } else {
                //返回没有收到数据
                RaiseCommandCompleteEvent(oEvent);
            }
            return true;
        } else {
            return false;
        }
    }

    /**
     * 分析缓冲中的数据包
     */
    protected void Analysis(int iCardSize) throws Exception {
        ReadCardDataBase_Result result = (ReadCardDataBase_Result) _Result;
        if (iCardSize == 0) {
            iCardSize = 1024;
        }
        //result.DataBaseSize = iCardSize;

        ArrayList<CardDetail> CardList = new ArrayList<>(iCardSize);
        result.CardList = CardList;
        //byte bCardBuf[] = new byte[0x21];

        while (mBufs.peek() != null) {
            ByteBuf buf = mBufs.poll();
            iCardSize = buf.readInt();
            //buf.readBytes(bCardBuf,0,0x21);
            for (int i = 0; i < iCardSize; i++) {
                CardDetail cd = new CardDetail();
                cd.SetBytes(buf);
                CardList.add(cd);
                //iCardSize++;
            }
            buf.release();
        }
        
        result.DataBaseSize = CardList.size();

    }

    @Override
    protected void CommandOver_ReSend() {
        ClearBuf();
        _ProcessMax = mRecordCardSize;
        _ProcessStep = 0;
    }

    protected void ClearBuf() {
        if (mBufs == null) {
            return;
        }
        while (mBufs.peek() != null) {
            ByteBuf buf = mBufs.poll();
            buf.release();
        }
    }
}

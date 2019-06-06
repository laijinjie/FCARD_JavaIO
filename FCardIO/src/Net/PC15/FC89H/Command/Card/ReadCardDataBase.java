/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Net.PC15.FC89H.Command.Card;

import Net.PC15.Connector.INConnectorEvent;
import Net.PC15.FC8800.Command.Card.Parameter.ReadCardDataBase_Parameter;
import Net.PC15.FC8800.Command.Card.Result.ReadCardDatabaseDetail_Result;
import Net.PC15.FC8800.Command.FC8800Command;
import Net.PC15.FC89H.Command.Card.Result.ReadCardDataBase_Result;
import Net.PC15.FC89H.Command.Data.CardDetail;
import Net.PC15.FC8800.Packet.FC8800PacketModel;
import Net.PC15.Util.ByteUtil;
import io.netty.buffer.ByteBuf;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * 从控制器中读取卡片数据，针对FC89H使用<br/>
 * 成功返回结果参考 {@link ReadCardDataBase_Result}
 *
 * @author 徐铭康
 */
public class ReadCardDataBase extends FC8800Command {
    protected int mStep;//指示当前命令进行的步骤
    protected ConcurrentLinkedQueue<ByteBuf> mBufs;
    protected int mRecordCardSize;//记录的卡数量
    public ReadCardDataBase(ReadCardDataBase_Parameter par) {
        //super(par);
        _Parameter = par;
        _ProcessMax = 2;
        _ProcessStep = 1;

        mStep = 1;//第一步，读取卡片存储信息，并分析出是否已存储卡片
        CreatePacket(7, 1);
    }
    
    
    @Override
    protected boolean _CommandStep(INConnectorEvent oEvent, FC8800PacketModel model) {
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
    protected boolean CheckDataBaseDetail(INConnectorEvent oEvent, FC8800PacketModel model) {
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
    private boolean CheckReadCardPacket(INConnectorEvent oEvent, FC8800PacketModel model) {
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
            if (iCardSize > 0) {
                //Calendar begintime = Calendar.getInstance();
                //开始拆分接收到的数据包
                Analysis(iCardSize);
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
    private void Analysis(int iCardSize) {
        ReadCardDataBase_Result result = (ReadCardDataBase_Result) _Result;
        result.DataBaseSize = iCardSize;

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
            }
            buf.release();
        }

    }

    @Override
    protected void Release0() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}

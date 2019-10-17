/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Net.PC15.FC8800.Command.TimeGroup;
import Net.PC15.FC8800.Command.TimeGroup.Result.ReadTimeGroup_Result;
import Net.PC15.Command.CommandParameter;
import Net.PC15.Connector.INConnectorEvent;
import Net.PC15.FC8800.Command.Data.CardDetail;
import Net.PC15.FC8800.Command.Data.TimeGroup.WeekTimeGroup;
import Net.PC15.FC8800.Command.FC8800Command;
import Net.PC15.FC8800.Packet.FC8800PacketModel;
import io.netty.buffer.ByteBuf;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * 读取所有开门时段
 * @author 徐铭康
 */
public class ReadTimeGroup extends FC8800Command {
    
    protected int mStep;//指示当前命令进行的步骤
    protected ConcurrentLinkedQueue<ByteBuf> mBufs;
    protected int mRecordCardSize;//记录的卡数量
    public ReadTimeGroup(CommandParameter par ){
        _Parameter = par;
         mBufs = new ConcurrentLinkedQueue<ByteBuf>();
         CreatePacket(6, 2);
    }
    @Override
    protected void Release0() {
         ClearBuf();
    }
    protected void ClearBuf() {
        if (mBufs == null) {
            return;
        }
        while (mBufs.peek() != null) {
            ByteBuf buf = mBufs.poll();
            buf.release();
        }
        _Result = null;
    }
    
    @Override
    protected boolean _CommandStep(INConnectorEvent oEvent, FC8800PacketModel model) {
        if (CheckResponse_Cmd(model, 6, 2, 0)) {
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

        } else if (CheckResponse_Cmd(model, 6, 2, 0xFF, 4)) {
            ByteBuf buf = model.GetDatabuff();
            int iCardSize = buf.readInt();
            if (iCardSize > 0) {
                CommandWaitResponse();
                //Calendar begintime = Calendar.getInstance();
                //开始拆分接收到的数据包
                try {
                    _Result = Analysis(iCardSize);
                }
                catch (Exception e){
                    
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
    protected ReadTimeGroup_Result Analysis(int iSize) throws Exception {
        ReadTimeGroup_Result result = new ReadTimeGroup_Result();
        
        result.DataBaseSize = iSize;
        ArrayList<WeekTimeGroup> list = new ArrayList<>(iSize);
        /**/
        while (mBufs.peek() != null) {
            ByteBuf buf = mBufs.poll();
            WeekTimeGroup wtg = new WeekTimeGroup(8);
            wtg.SetBytes(buf);
            list.add(wtg);
            buf.release();
        }

        for (ByteBuf buf : mBufs){
        
        }
        result.List = list;
        return result;
    }
}

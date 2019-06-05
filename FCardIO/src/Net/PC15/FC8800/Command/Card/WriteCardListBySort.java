/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Net.PC15.FC8800.Command.Card;

import Net.PC15.Connector.INConnectorEvent;
import Net.PC15.FC8800.Command.Card.Parameter.WriteCardListBySort_Parameter;
import Net.PC15.FC8800.Command.Card.Result.ReadCardDatabaseDetail_Result;
import Net.PC15.FC8800.Command.Card.Result.WriteCardListBySort_Result;
import Net.PC15.FC8800.Command.Data.CardDetail;
import Net.PC15.FC8800.Command.FC8800Command;
import Net.PC15.FC8800.Packet.FC8800PacketCompile;
import Net.PC15.FC8800.Packet.FC8800PacketModel;
import Net.PC15.Util.ByteUtil;
import io.netty.buffer.ByteBuf;
import java.util.ArrayList;
import java.util.Collections;

/**
 * 将卡片列表写入到控制器排序区<br/>
 * 控制器排序区每次写入都会清空重头写，所以添加少量卡(小于100)时应使用{@link WriteCardListBySort}<br/>
 * 成功返回结果参考 {@link WriteCardListBySort_Result}
 *
 * @author 赖金杰
 */
public class WriteCardListBySort extends FC8800Command {

    protected int mIndex;//指示当前命令进行的步骤
    protected ArrayList<CardDetail> _List;
    protected int mStep;
    /**
     * 本次上传卡数量
     */
    protected int mUploadMax;

    public WriteCardListBySort(){
        
    }
    
    public WriteCardListBySort(WriteCardListBySort_Parameter par) {
        _Parameter = par;
        _List = par.CardList;
        _ProcessMax = par.CardList.size();
        mIndex = 0;
        mUploadMax = _List.size();

        CreatePacket(7, 1);
        mStep = 1;//第一步读取存储空间
    }

    @Override
    protected void Release0() {

        _Parameter = null;
        _Result = null;
        _List = null;
        return;
    }

    @Override
    protected boolean _CommandStep(INConnectorEvent oEvent, FC8800PacketModel model) {
        switch (mStep) {
            case 1://读取卡空间信息
                return CheckDataBaseDetail(model);
            case 2://发送初始化排序区指令
                return CheckBeginWriteResponse(model);
            case 3://写排序区卡
                return CheckWriteCardResponse(model);
            case 4://发送终止写入指令
                return CheckEndWriteResponse(oEvent, model);
        }
        return false;

    }

    /**
     * 检查卡片数据库的信息，获取卡片的容量数据
     *
     * @param model 本次数据包的包装类
     * @return true 正确解析或 false 未解析
     */
    protected boolean CheckDataBaseDetail(FC8800PacketModel model) {
        if (CheckResponse_Cmd(model, 7, 1, 0, 0x10)) {
            ByteBuf buf = model.GetDatabuff();

            ReadCardDatabaseDetail_Result r = new ReadCardDatabaseDetail_Result();
            int iDataBaseSize = buf.readInt();

            if (iDataBaseSize < mUploadMax) {
                mUploadMax = iDataBaseSize;
            }

            SendBeginWrite();

        } else {
            return false;
        }
        return true;
    }

    /**
     * 发送开始写排序区卡指令
     */
    private void SendBeginWrite() {
        _ProcessStep = 1;

        //发送开始指令
        CreatePacket(7, 7, 0);
        CommandReady();
        mStep = 2;
    }

    /**
     * 检查发送开始指令的返回值
     *
     * @param model 本次数据包的包装类
     * @return true 正确解析或 false 未解析
     */
    private boolean CheckBeginWriteResponse(FC8800PacketModel model) {
        if (CheckResponseOK(model)) {
            IniWriteCard();
        } else {
            return false;
        }
        return true;
    }

    /**
     * 初始化写卡操作的资源
     */
    private void IniWriteCard() {
        _ProcessStep = 2;
        //初始化缓冲空间
        int iLen = (10 * 0x21) + 8;
        ByteBuf dataBuf = ByteUtil.ALLOCATOR.buffer(iLen);

        CreatePacket(7, 7, 1, iLen, dataBuf);
        mIndex = 0;
        
        Collections.sort(_List);
        WriteNext();
        CommandReady();
        mStep = 3;
    }

    /**
     * 写入下一个卡号
     */
    private void WriteNext() {
        int iMaxSize = 10; //每个数据包最大15个卡
        int iSize = 0;
        int iIndex = 0;

        FC8800PacketCompile compile = (FC8800PacketCompile) _Packet;
        FC8800PacketModel p = (FC8800PacketModel) _Packet.GetPacket();
        ByteBuf dataBuf = p.GetDatabuff();
        dataBuf.clear();
        dataBuf.writeInt(mIndex + 1);
        dataBuf.writeInt(iMaxSize);
        for (int i = mIndex; i < mUploadMax; i++) {
            iIndex = i;
            iSize += 1;
            CardDetail cd = _List.get(iIndex);
            cd.GetBytes(dataBuf);
            if (iSize == iMaxSize) {
                break;
            }
        }
        if (iSize != iMaxSize) {
            dataBuf.setInt(4, iSize);
        }
        p.SetDataLen(dataBuf.readableBytes());//重置数据长度
        compile.Compile();//重新编译
        mIndex = iIndex + 1;
        CommandReady();
    }

    /**
     * 检查写排序区卡的返回
     *
     * @param model 本次数据包的包装类
     * @return true 正确解析或 false 未解析
     */
    protected boolean CheckWriteCardResponse(FC8800PacketModel model) {
        if (CheckResponseOK(model)) {
            _ProcessStep = mIndex;

            if (mIndex < mUploadMax) {
                WriteNext();
            } else {
                WriteCardListBySort_Result r = new WriteCardListBySort_Result();
                int ListSize = _List.size();
                if (_List.size() > mUploadMax) {
                    r.FailTotal = ListSize - mUploadMax;
                    ArrayList<CardDetail> failList = new ArrayList<>(r.FailTotal);
                    for (int i = mIndex; i < ListSize; i++) {
                        failList.add(_List.get(i));
                    }
                    r.CardList = failList;
                }
                _Result = r;

                SendEndWrite();
            }

        } else {
            return false;
        }
        return true;
    }

    /**
     * 发送结束写指令
     */
    private void SendEndWrite() {
        //发送结束指令
        CreatePacket(7, 7, 2);
        CommandReady();
        mStep = 4;
    }

    /**
     * 检查结束写排序区卡命令的返回
     *
     * @param oEvent 事件句柄
     * @param model 本次数据包的包装类
     * @return true 正确解析或 false 未解析
     */
    protected boolean CheckEndWriteResponse(INConnectorEvent oEvent, FC8800PacketModel model) {
        if (CheckResponseOK(model)) {
            RaiseCommandCompleteEvent(oEvent);
        } else {
            return false;
        }
        return true;
    }

}

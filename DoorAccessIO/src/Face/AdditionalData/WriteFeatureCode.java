/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Face.AdditionalData;

import Door.Access.Command.E_CommandStatus;
import Door.Access.Connector.INConnectorEvent;
import Door.Access.Door8800.Command.Door8800Command;
import Door.Access.Door8800.Packet.Door8800PacketModel;
import Door.Access.Packet.INPacketModel;
import Door.Access.Util.ByteUtil;
import Face.AdditionalData.Parameter.WriteFeatureCode_Parameter;
import Face.AdditionalData.Result.WriteFeatureCode_Result;
import io.netty.buffer.ByteBuf;

/**
 * 写入人脸指纹特征
 *
 * @author F
 */
public class WriteFeatureCode extends Door8800Command {

    /**
     * 写索引
     */
    private int _WriteIndex = 0;

    /**
     * 文件句柄
     */
    private long _FileHandle = 0;

    /**
     * 操作步骤
     */
    private int _Step = 0;

    /**
     * 写入特征码返回结果
     */
    private WriteFeatureCode_Result mResult;

    /**
     * 写入人脸指纹特征
     *
     * @param par 写入人脸指纹特征参数
     */
    public WriteFeatureCode(WriteFeatureCode_Parameter par) {
        _Parameter = par;
        int iLen = 6;
        ByteBuf dataBuf = ByteUtil.ALLOCATOR.buffer(iLen);
        dataBuf.writeInt((int) par.getUserCode());
        dataBuf.writeByte(par.getType());
        dataBuf.writeByte(par.getSerialNumber());
        //分类 0x0B,命令0x01,参数0x00,数据用户号（4字节）文件类型（1字节）序号（1字节）
        CreatePacket(0x0B, 1, 0, iLen, dataBuf);
        mResult = new WriteFeatureCode_Result();
        _Result = mResult;
    }

    @Override
    protected void Release0() {
        return;
    }

    @Override
    protected boolean CommandStep(INConnectorEvent oEvent, INPacketModel model) {
        Door8800PacketModel pmodel = (Door8800PacketModel) model;

        Door8800PacketModel SendModel = (Door8800PacketModel) _Packet.GetPacket();
        if (pmodel.GetCode() != SendModel.GetCode()) {
            //检查命令标志是否一致
            return false;
        }

        if (CheckResponse_PasswordErr(pmodel)) {
            CommandOver();//发生错误
            RaisePasswordErrorEvent(oEvent);
            return false;
        }

        if (CheckResponse_CheckSumErr(pmodel)) {
            _Status = E_CommandStatus.OnReady; //重新发送
            if (_SendCount > 100) {
                CommandOver();//发生错误
                RaiseChecksumErrorEvent(oEvent);
            }
            return false;
        }
        return _CommandStep(oEvent, pmodel);
    }

    @Override
    protected boolean _CommandStep(INConnectorEvent oEvent, Door8800PacketModel model) {
        CommandNext(oEvent, model);
        return true;
    }

    /**
     * 命令继续执行
     *
     * @param oEvent
     * @param model
     */
    protected void CommandNext(INConnectorEvent oEvent, Door8800PacketModel model) {
        switch (_Step) {
            case 0:
                CheckOpenFileResult(oEvent, model);
                break;
            case 1:
                CheckWriteFileResult(oEvent, model);
                break;
            case 2:
                if (CheckResponse_Cmd(model, 0x0B, 3, 0, 1)) {
                    mResult.Success = model.GetDatabuff().readByte();
                    RaiseCommandCompleteEvent(oEvent);
                }
                break;
            default:
                break;
        }

        //  
    }

    /**
     * 写入文件
     *
     * @param oEvent
     * @param model
     */
    protected void CheckOpenFileResult(INConnectorEvent oEvent, Door8800PacketModel model) {

        ByteBuf buf = model.GetDatabuff();
        _FileHandle = buf.readInt();
        mResult.FileHandle = _FileHandle;
        if (_FileHandle == 0) {
            mResult.Success = 0;
            RaiseChecksumErrorEvent(oEvent);
            return;
        }
        WriteFeatureCode_Parameter par = (WriteFeatureCode_Parameter) _Parameter;
        int iPackSize = 1024;
        int iDataLen = par.getDatas().length;
        if (iPackSize > iDataLen) {
            iPackSize = iDataLen;
        }
        _ProcessMax = iDataLen;
        _ProcessStep = 0;
        int iLen = 7 + iPackSize;
        ByteBuf dataBuf = ByteUtil.ALLOCATOR.buffer(iLen);

        dataBuf.writeInt((int) _FileHandle);
        _WriteIndex = 0;
        dataBuf.writeMedium(_WriteIndex);
        dataBuf.writeBytes(par.getDatas(), _WriteIndex, iPackSize);
        _Step = 1;
        CreatePacket(0x0B, 2, 0, iLen, dataBuf);
    }

    private void CheckWriteFileResult(INConnectorEvent oEvent, Door8800PacketModel model) {
        if (CheckResponse_Cmd(model, 0x0B, 2, 0)) {
            int iPackSize = 1024;
            _WriteIndex += iPackSize;
            _ProcessStep += iPackSize;
            WriteFeatureCode_Parameter par = (WriteFeatureCode_Parameter) _Parameter;
            int iDataLen = par.getDatas().length - _WriteIndex;
            if (iDataLen > iPackSize) {
                iDataLen = iPackSize;
            }
            if (iDataLen <= 0) {
                _ProcessStep = _ProcessMax;
                int dataLen = par.getDatas().length;
                long crc32 = ByteUtil.CreateCRC32(par.getDatas(), 0, dataLen);
                int iLen = 4;
                ByteBuf dataBuf = ByteUtil.ALLOCATOR.buffer(iLen);
                dataBuf.writeInt((int) crc32);
                _Step = 2;
                CreatePacket(0x0b, 0x03, 0x00, iLen, dataBuf);
            } else {
                ByteBuf dataBuf = model.GetDatabuff().clear();
                dataBuf.writeInt((int) _FileHandle);
                dataBuf.writeMedium(_WriteIndex);
                dataBuf.writeBytes(par.getDatas(), _WriteIndex, iPackSize);
                model.SetDatabuff(dataBuf);
            }
        } else if (CheckResponse_Cmd(model, 0x0B, 2, 2)) {
            CreatePacket(0x0B, 1, 0, 6, model.GetDatabuff());
        }
    }
}

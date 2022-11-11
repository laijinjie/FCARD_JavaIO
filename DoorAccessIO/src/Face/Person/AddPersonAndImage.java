package Face.Person;

import Door.Access.Connector.INConnectorEvent;
import Door.Access.Door8800.Command.Door8800Command;
import Door.Access.Door8800.Packet.Door8800PacketModel;
import Door.Access.Util.ByteUtil;
import Face.Data.IdentificationData;
import Face.Person.Parameter.AddPersonAndImage_Parameter;
import Face.Person.Result.AddPersonAndImage_Result;
import io.netty.buffer.ByteBuf;

/**
 * 添加人员并上传人员识别信息
 *
 * @author F
 */
public class AddPersonAndImage extends Door8800Command {

    AddPersonAndImage_Result mResult;
    AddPersonAndImage_Parameter mPar;
    private long UserCode;
    private int _FileIndex = 0;
    private int _Step = 0;
    private int IdentSize = 0;
    private int WriteIndex = 0;
    private int _WriteStep = 0;
    private long FileHandle = 0;

    /**
     * 添加人员并上传人员识别信息
     *
     * @param parameter 添加人员并上传人员识别信息参数
     */
    public AddPersonAndImage(AddPersonAndImage_Parameter parameter) {
        _Parameter = parameter;
        IdentSize = parameter.IdentificationDatas.size();
        mPar = parameter;
        UserCode = parameter.PersonDetail.UserCode;
        CreatePacket0();

    }

    private void CreatePacket0() {
        mResult = new AddPersonAndImage_Result(mPar.PersonDetail, mPar.IdentificationDatas);
        UserCode = mPar.PersonDetail.UserCode;
        ByteBuf dataBuf = ByteUtil.ALLOCATOR.buffer(0xA2);
        dataBuf.writeByte(1);
        mPar.getBytes(dataBuf);
        CreatePacket(0x07, 0x04, 0x00, 0xA2, dataBuf);
        _Result = mResult;
    }

    @Override
    protected boolean _CommandStep(INConnectorEvent oEvent, Door8800PacketModel model) {
        switch (_Step) {
            case 0:
                if (CheckResponseOK(model)) {
                    mResult.UserUploadStatus = true;
                    return CheckFile(oEvent,model);
                } else if (CheckResponse_Cmd(model, 0x07, 0x04, 0xFF)) {
                    mResult.UserUploadStatus = false;
                    RaiseCommandCompleteEvent(oEvent);
                    return true;
                }
                break;
            case 1:
                return CheckFile(oEvent,model);
            default:
                break;
        }


        return false;
    }
public boolean CheckFile(INConnectorEvent oEvent, Door8800PacketModel model){
    IdentificationData data = mPar.IdentificationDatas.get(_FileIndex);
    if (data == null || data.DataBuf == null) {
       // mResult.UserUploadStatus = false;
        mResult.IdDataUploadStatus.set(_FileIndex, 2);
        RaiseCommandCompleteEvent(oEvent);
        return false;
    } else {
        WriteFie(oEvent, model, data);
        _Step = 1;
    }
    return true ;
}

    private void WriteFie(INConnectorEvent oEvent, Door8800PacketModel model, IdentificationData data) {
        switch (_WriteStep) {
            case 0:
                BeginWrite(oEvent, model, data);
                break;
            case 1:
                if (CheckResponse_Cmd(model, 4)) {
                    StartWrite(oEvent, model, data);
                }
                if (CheckResponse_Cmd(model, 0x0b, 0x02)) {
                    StartWrite(oEvent, model, data);
                }
                break;
            case 2:
                EndWrite(oEvent, model, data);
                break;
            default:
                break;
        }
    }

    /**
     * 开始写入文件
     *
     * @param data
     */
    private void BeginWrite(INConnectorEvent oEvent, Door8800PacketModel model, IdentificationData data) {

        ByteBuf dataBuf = ByteUtil.ALLOCATOR.buffer(6);
        dataBuf.writeInt((int) UserCode);
        dataBuf.writeByte(data.DataType);
        dataBuf.writeByte(data.DataNum);
        _WriteStep = 1;
        CreatePacket(0x0B, 0x01, 0x00, 6, dataBuf);

    }

    private void StartWrite(INConnectorEvent oEvent, Door8800PacketModel model, IdentificationData data) {
        ByteBuf buf = model.GetDatabuff();
        if (FileHandle == 0) {
            FileHandle = buf.readUnsignedInt();
        }
        if (FileHandle == 0) {
            mResult.IdDataUploadStatus.set(_FileIndex, -1);
            RaiseCommandCompleteEvent(oEvent);
            return;
        }
        int iPackSize = 1024;
        _ProcessMax = data.DataBuf.length;

        int iDataLen = data.DataBuf.length - WriteIndex;
        _ProcessStep = WriteIndex;

        if (iDataLen > iPackSize) {
            iDataLen = iPackSize;
        }
        //  int index = WriteIndex + iDataLen;
        if (iDataLen <= 0) {
            _ProcessStep = _ProcessMax;
            long crc = ByteUtil.CreateCRC32(data.DataBuf, 0, data.DataBuf.length);
            buf = ByteUtil.ALLOCATOR.buffer(4);
            buf.writeInt((int) crc);
            _WriteStep = 2;
            CreatePacket(0x0b, 0x03, 0x00, 4, buf);
            return;
        }
        int iBufSize = 7 + iDataLen;
        buf = ByteUtil.ALLOCATOR.buffer(iBufSize);
        buf.writeInt((int) FileHandle);
        buf.writeMedium(WriteIndex);
        buf.writeBytes(data.DataBuf, WriteIndex, iDataLen);
        CreatePacket(0x0b, 0x02, 0x00, iBufSize, buf);
        WriteIndex += iDataLen;
    }

    private void EndWrite(INConnectorEvent oEvent, Door8800PacketModel model, IdentificationData data) {
        if (CheckResponse_Cmd(model, 0x0b, 0x03)) {
            int iStatus = model.GetDatabuff().readByte();
            mResult.IdDataUploadStatus.set(_FileIndex, iStatus);
            if (iStatus == 4) {
                mResult.IdDataRepeatUser.set(0, (long) UserCode);
                if (!mPar.WaitRepeatMessage) {
                    WriteNextImage(oEvent, model);
                    return;
                }
            } else {
                WriteNextImage(oEvent, model);
            }
        }
    }

    private void WriteNextImage(INConnectorEvent oEvent, Door8800PacketModel model) {
        if ((_FileIndex + 1) == IdentSize) {
            RaiseCommandCompleteEvent(oEvent);
            return;
        }
        _FileIndex += 1;
        WriteIndex = 0;
        FileHandle = 0;
        _WriteStep=0;
        CheckFile(oEvent, model);
    }

    @Override
    protected void Release0() {

    }
}

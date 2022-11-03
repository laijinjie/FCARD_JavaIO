package Face.Person;

import Door.Access.Command.CommandDetail;
import Door.Access.Connector.INConnectorEvent;
import Door.Access.Door8800.Command.Door8800Command;
import Door.Access.Door8800.Packet.Door8800PacketModel;
import Door.Access.Util.ByteUtil;
import Face.AdditionalData.Result.ReadFile_Result;
import Face.Data.IdentificationData;
import Face.Data.Person;
import Face.Person.Parameter.RegisterIdentificationData_Parameter;
import Face.Person.Result.RegisterIdentificationData_Result;
import io.netty.buffer.ByteBuf;

import java.io.UnsupportedEncodingException;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 注册人员指纹特征码或注册人员头像
 *
 * @author F
 */
public class RegisterIdentificationData extends Door8800Command {

    private int mStep;
    private int UserCode;
    private ReadFile_Result mReadFileResult;
    private int FileType;
    private int _FileStep;

    private byte[] _FileDatas;
/**
 * 注册人员指纹特征码或注册人员头像
 * @param parameter 注册人员指纹特征码或注册人员头像参数
 */
    public RegisterIdentificationData(RegisterIdentificationData_Parameter parameter) {
        _Parameter = parameter;
        _Result = new RegisterIdentificationData_Result(parameter.PersonDetail, parameter.DataType, parameter.DataNum);
        UserCode = parameter.PersonDetail.UserCode;
        _ProcessMax = 6;
        CreatePacket0(parameter.PersonDetail);
    }

    private void CreatePacket0(Person PersonDetail) {
        int len = 162;
        ByteBuf buf = ByteUtil.ALLOCATOR.buffer(len);
        buf.writeByte(1);
        try {
            PersonDetail.getBytes(buf);
        } catch (UnsupportedEncodingException e) {
            System.out.println(e.getMessage());
            return;
        }
        mStep = 1;
        CreatePacket(0x07, 0x04, 0x00, len, buf);
    }

    @Override
    protected boolean _CommandStep(INConnectorEvent oEvent, Door8800PacketModel model) {
        /**
         * 注册流程
         * 1、注册人员信息
         * 2、人脸机拍照注册开始命令（判断之前是否存在图片，如果存在先删除旧图片）
         * 3、注册命令反馈
         * 4、注册命令结果
         * 5、读取人员信息
         * 6、读取文件信息
         */
        RegisterIdentificationData_Parameter parameter = (RegisterIdentificationData_Parameter) _Parameter;
        boolean result = false;
        switch (mStep) {
            /**
             * 添加人员返回值处理
             */
            case 1:
                /**
                 * 注册成功
                 */
                if (CheckResponseOK(model)) {
                    if (parameter.DataType < 4) {
                        result = DeleteFile(parameter);
                    } else {
                        result= BeginReg(parameter);
                    }
                    RaiseCommandProcessEvent(oEvent);
                }
                /**
                 * 注册失败
                 */
                if (CheckResponse_Cmd(model, 0x07, 0x04, 0xff)) {
                    RegisterIdentificationData_Result data_result = (RegisterIdentificationData_Result) _Result;
                    data_result.Status = 5;
                }
                break;
            /**
             * 开始注册
             */
            case 2:
                if (CheckResponseOK(model)) {
                    result= BeginReg(parameter);
                }
                break;
            /**
             * 注册命令反馈
             */
            case 3:
                if (CheckResponse_Cmd(model, 1)) {
                    result = RegFeedBack(model, parameter);
                    RaiseCommandProcessEvent(oEvent);
                    if (result) {
                        CommandWaitResponse();
                    }
                }
                break;
            /**
             * 等待注册结果返回
             */
            case 4:
                if (model.GetCmdType() == 0x19 && model.GetCmdIndex() == 0xF0) {
                    int iStatus = model.GetDatabuff().readByte();
                    RegisterIdentificationData_Result data_result = (RegisterIdentificationData_Result) _Result;
                    data_result.Status = 100 + iStatus;
                    if (iStatus == 1) {

                        ReadIdentificationData(parameter);
                        RaiseCommandProcessEvent(oEvent);
                        result=true;
                    }
                }
                break;
            /**
             * 读取人员详情
             */
            case 5:
                if (CheckResponse_Cmd(model, 0xA1)) {
                    result = ReadPersonDetail(model);
                }
                break;
            /**
             * 读取文件
             */
            case 6:
                result = ReadFile(model, oEvent);
                break;
            default:
                break;
        }
        if (!result) {
            RaiseCommandCompleteEvent(oEvent);
        }
        return result;
    }


    /**
     * 删除文件
     *
     * @param parameter
     * @return
     */
    private boolean DeleteFile(RegisterIdentificationData_Parameter parameter) {
        try {
            int iBufLen = 20;
            ByteBuf buf = ByteUtil.ALLOCATOR.buffer(iBufLen);
            buf.writeInt(parameter.PersonDetail.UserCode);
            buf.writeByte(parameter.DataType == 3 ? 1 : 0);
            buf.writeInt(0);
            int iLen = 10;
            int iIndex = 0;
            while (iIndex < iLen) {
                int iValue = 0;
                if (parameter.DataType == 1) {
                    iValue = parameter.DataNum == iIndex ? iIndex : 0;
                }
                buf.writeByte(iValue);
                iIndex++;
            }
            buf.writeByte(parameter.DataType == 2 ? 1 : 0);
            mStep = 2;
            _ProcessStep = 2;
            CreatePacket(0x0B, 0x06, 0x00, iBufLen, buf);
            return true;
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        return false;
    }

    /**
     * 开始注册信息
     *
     * @param parameter
     * @return
     */
    private boolean BeginReg(RegisterIdentificationData_Parameter parameter) {
        int iBufLen = 6;
        ByteBuf buf = ByteUtil.ALLOCATOR.buffer(iBufLen);
        buf.writeByte(parameter.DataType);
        buf.writeInt(parameter.PersonDetail.UserCode);
        buf.writeByte(parameter.DataNum);
        mStep = 3;
        _ProcessStep = 3;
        CommandDetail commandDetail = parameter.getCommandDetail();
        commandDetail.Timeout = 4000;
        commandDetail.RestartCount = 0;
        CreatePacket(0x07, 0x20, 0x00, iBufLen, buf);
        return true ;
    }

    /**
     * 注册命令反馈
     *
     * @param model
     * @param parameter
     * @return
     */
    private boolean RegFeedBack(Door8800PacketModel model, RegisterIdentificationData_Parameter parameter) {
        int iStatus = model.GetDatabuff().readByte();
        RegisterIdentificationData_Result result = (RegisterIdentificationData_Result) _Result;
        result.Status = iStatus;
        if (iStatus == 1) {
            _ProcessStep = 4;
            mStep = 4;
            CommandDetail commandDetail = parameter.getCommandDetail();
            commandDetail.Timeout = 10000;
            commandDetail.RestartCount = 0;
            return true;
        }
        return false;
    }

    /**
     * 注册命令返回结果
     *
     * @param model
     * @param parameter
     * @return
     */
    private boolean RegResult(Door8800PacketModel model, RegisterIdentificationData_Parameter parameter) {
        int iStatus = model.GetDatabuff().readByte();
        RegisterIdentificationData_Result result = (RegisterIdentificationData_Result) _Result;
        result.Status = 100 + iStatus;
        if (iStatus == 1) {

            return true;
        } else {
            return false;
        }
    }

    private void ReadIdentificationData(RegisterIdentificationData_Parameter parameter) {
        _ProcessStep = 5;
        CommandDetail commandDetail = parameter.getCommandDetail();
        ByteBuf buf;
        int iBufLen;
        if (parameter.DataType == 4 || parameter.DataType == 5) {

            commandDetail.Timeout = 3000;
            commandDetail.RestartCount = 5;
            mStep = 5;
            iBufLen = 4;
            buf = ByteUtil.ALLOCATOR.buffer(iBufLen);
            buf.writeInt(UserCode);
            CreatePacket(0x07, 0x03, 0x01, iBufLen, buf);
        } else {
            iBufLen = 6;
            mStep = 6;
            commandDetail.Timeout = 800;
            commandDetail.RestartCount = 5;
            mReadFileResult = new ReadFile_Result();
            byte[] bDownloadTable = new byte[10];
            /*指纹*/
            bDownloadTable[1] = 2;
            /*红外人脸特征码*/
            bDownloadTable[2] = 4;
            /*人员头像*/
            bDownloadTable[3] = 1;
            FileType = bDownloadTable[parameter.DataType];
            buf = ByteUtil.ALLOCATOR.buffer(iBufLen);
            buf.writeByte(FileType);
            buf.writeByte(parameter.DataNum);
            buf.writeInt(UserCode);
            CreatePacket(0x0B, 0x15, 0x00, iBufLen, buf);
        }
    }

    private boolean ReadPersonDetail(Door8800PacketModel model) {
        try {
            RegisterIdentificationData_Result result = (RegisterIdentificationData_Result) _Result;
            result.PersonDetail.setBytes(model.GetDatabuff());
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return false;
    }

    private boolean ReadFile(Door8800PacketModel model, INConnectorEvent oEvent) {
        boolean result = false;
        switch (_FileStep) {
            case 0:
                result = CheckOpenFileResult(model);
                break;
            case 1:
                result = CheckReadFileBlockResult(model);
                break;
            case 2:
                result = CheckReadFile(model);
                if (result) {
                    RaiseCommandCompleteEvent(oEvent);
                }
                break;
            default:
                break;
        }
        return result;
    }


    /**
     * 开始读取文件句柄
     *
     * @param model
     * @return 是否成功
     */
    private boolean CheckOpenFileResult(Door8800PacketModel model) {
        if (CheckResponse_Cmd(model, 0x0B, 0x15, 1, 17)) {
            mReadFileResult.setBytes(model.GetDatabuff());
            if (mReadFileResult.UserCode != UserCode || mReadFileResult.FileType != FileType) {
                return false;
            }
            if (mReadFileResult.FileHandle == 0 || mReadFileResult.FileSize == 0) {
                return false;
            }
            _FileDatas = new byte[mReadFileResult.FileSize];
            _ProcessMax = mReadFileResult.FileSize;
            _FileStep = 1;
            _ProcessStep = 0;
            AtomicInteger iPackSize = new AtomicInteger(1024);
            if (iPackSize.get() > mReadFileResult.FileSize) {
                iPackSize.set(mReadFileResult.FileSize);
            }
            _ProcessMax = mReadFileResult.FileSize;
            int iBufLen = 10;
            ByteBuf buf = ByteUtil.ALLOCATOR.buffer(iBufLen);
            buf.writeInt(mReadFileResult.FileHandle);
            buf.writeInt(0);
            buf.writeShort(iPackSize.get());
            CreatePacket(0x0b, 0x015, 0x02, iBufLen, buf);
            return true;
        }
        return false;
    }

    /**
     * 读取文件块
     *
     * @param model
     * @return 是否成功
     */
    private boolean CheckReadFileBlockResult(Door8800PacketModel model) {
        if (CheckResponse_Cmd(model, 0x0b, 0x15, 0x02)) {
            int FileHandle;
            int iDataIndex;
            int iSize = 0;
            ByteBuf buf = model.GetDatabuff();
            FileHandle = buf.readInt();
            iDataIndex = buf.readInt();
            iSize = buf.readUnsignedShort();
            long crc = buf.readUnsignedInt();
            _ProcessStep = iDataIndex + iSize;
            buf.readBytes(_FileDatas, iDataIndex, iSize);
            long filecrc=ByteUtil.CreateCRC32(_FileDatas,iDataIndex, iSize);
            if (crc == filecrc) {
                int iPackSize = 1024;
                iDataIndex += iPackSize;
                int iDataLen = mReadFileResult.FileSize - iDataIndex;
                if (iDataLen > iPackSize) {
                    iDataLen = iPackSize;
                }
                buf = ByteUtil.ALLOCATOR.buffer(10);
                buf.writeInt(FileHandle);
                if (iDataLen <= 0) {
                    _ProcessStep = _ProcessMax;
                    _FileStep = 2;
                    CreatePacket(0x0b, 0x015, 0x03, 4, buf);
                } else {
                    buf.writeInt(iDataIndex);
                    buf.writeShort(iDataLen);
                    CreatePacket(0x0b, 0x015, 0x02, 10, buf);
                }
                return true;
            }
        }
        return false;
    }

    /**
     * 关闭文件句柄
     *
     * @param model
     * @return 是否成功
     */
    private boolean CheckReadFile(Door8800PacketModel model) {
        if (CheckResponse_Cmd(model, 0x0B, 0x15, 3)) {
            long crc = ByteUtil.CreateCRC32(_FileDatas,0,_FileDatas.length);
            mReadFileResult.Result = (crc == mReadFileResult.FileCRC);
            _ProcessStep = _ProcessMax;
            if (mReadFileResult.Result) {
                RegisterIdentificationData_Result res = (RegisterIdentificationData_Result) _Result;
                int[] bTypeTable = new int[10];
                bTypeTable[1] = 2;
                bTypeTable[2] = 3;
                bTypeTable[3] = 1;
                int iType = bTypeTable[res.DataType];
                res.ResultData = new IdentificationData(iType, res.DataNum, _FileDatas);
            }
            _FileDatas = null;
            return true;
        }
        return false;
    }

    @Override
    protected void Release0() {

    }
}

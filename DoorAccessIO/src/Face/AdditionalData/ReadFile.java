package Face.AdditionalData;

import Door.Access.Command.CommandDetail;
import Door.Access.Connector.INConnectorEvent;
import Door.Access.Door8800.Command.Door8800Command;
import Door.Access.Door8800.Packet.Door8800PacketModel;
import Door.Access.Util.ByteUtil;
import Face.AdditionalData.Parameter.ReadFile_Parameter;
import Face.AdditionalData.Result.ReadFile_Result;
import io.netty.buffer.ByteBuf;

/**
 * 读取大型文件
 *
 * @author F
 */
public class ReadFile extends Door8800Command {

    int _Step = 0;
    ReadFile_Result mResult;
    ReadFile_Parameter mPar;
/**
 * 读取大型文件
 * @param parameter 文件类型参数
 */
    public ReadFile(ReadFile_Parameter parameter) {
        _Parameter = parameter;

        mResult = new ReadFile_Result();
        mResult.UserCode = parameter.UserCode;
        mResult.Result = false;
        _Result = mResult;
        mPar = parameter;
        beginRead();
    }

    private void beginRead() {
        ByteBuf buf = ByteUtil.ALLOCATOR.buffer();
        buf.writeByte(mPar.Type);
        buf.writeByte(mPar.SerialNumber);
        buf.writeInt(mPar.UserCode);
        CreatePacket(0x0B, 0x15, 0x00, 6, buf);
    }

    @Override
    protected boolean _CommandStep(INConnectorEvent oEvent, Door8800PacketModel model) {
        switch (_Step) {
            /**
             * 读取返回句柄
             */
            case 0:
                return readFileHandleResult(oEvent, model);
            /**
             * 读文件块
             */
            case 1:
                return readFileResult(oEvent, model);
            /**
             * 关闭文件
             */
            case 2:
                return closeFileResult(oEvent, model);
            default:
                break;
        }
        return getResult(false,oEvent);
    }

    /**
     * 句柄返回结果
     * @param oEvent
     * @param model
     * @return
     */
    private boolean readFileHandleResult(INConnectorEvent oEvent, Door8800PacketModel model) {
        boolean result = false;
        if (CheckResponse_Cmd(model, 0x0b, 0x15, 0x01, 17)) {
            ByteBuf buf = model.GetDatabuff();
            int iFileType = buf.readByte();
            int iFileUserCode = buf.readInt();
            long iFileHandle = buf.readUnsignedInt();
            int iFileSize = buf.readInt();
            if (iFileSize < 0) {
                return getResult(false, oEvent);
            }
            long iFileCRC = buf.readUnsignedInt();
            if (iFileUserCode != mPar.UserCode || mPar.Type != iFileType || iFileHandle == 0) {
                return getResult(false, oEvent);
            }
            if (iFileHandle == Integer.MAX_VALUE) {
                CommandDetail detail = mPar.getCommandDetail();
                detail.Timeout = 150000;
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                beginRead();
            } else {
                _ProcessMax = iFileSize;
                mResult.FileCRC = iFileCRC;
                mResult.FileHandle = (int) iFileHandle;
                mResult.FileDatas = new byte[iFileSize];
                _Step = 1;
                mResult.FileSize = iFileSize;
                CreatePacket(0x0B, 0x15, 2, 10, setBuf(10, 0, 1024));
            }
            return true;
        }

        return getResult(false, oEvent);
    }

    /**
     * 设置发送包内容
     * @param bufSize
     * @param iIndex
     * @param packSize
     * @return
     */
    private ByteBuf setBuf(int bufSize, int iIndex, int packSize) {
        ByteBuf buf = ByteUtil.ALLOCATOR.buffer(bufSize);
        buf.writeInt(mResult.FileHandle);
        buf.writeInt(iIndex);
        buf.writeShort(packSize);
        return buf;
    }

    /**
     * 文件快内容返回结果
     * @param oEvent
     * @param model
     * @return
     */
    private boolean readFileResult(INConnectorEvent oEvent, Door8800PacketModel model) {
        if (CheckResponse_Cmd(model, 0x0B, 0x15, 2)) {
            ByteBuf buf = model.GetDatabuff();

            int iDataIndex;
            int iSize = 0;

            mResult.FileHandle = buf.readInt();
            iDataIndex = buf.readInt();
            iSize = buf.readUnsignedShort();
            long crc = buf.readUnsignedInt();

            buf.readBytes(mResult.FileDatas, iDataIndex, iSize);
            long myCrc = ByteUtil.CreateCRC32(mResult.FileDatas, iDataIndex, iSize);
            if (crc != myCrc) {
                return getResult(false, oEvent);
            }
            //校验通过，读取下一包
            int iPackSize = 1024;

            iDataIndex += iPackSize;
            _ProcessStep = iDataIndex;

            int iDataLen = mResult.FileSize - iDataIndex;

            if (iDataLen > iPackSize) {
                iDataLen = iPackSize;
            }
            /**
             * 还有文件继续读
             */
            if (iDataLen > 0) {
                _ProcessStep=iDataIndex;
                CreatePacket(0x0B, 0x15, 2, 10, setBuf(10, iDataIndex, iDataLen));

                return true;
            }
            /* 读取文件完成，关闭句柄*/
            buf = ByteUtil.ALLOCATOR.buffer(4);
            buf.writeInt(mResult.FileHandle);
            _Step=2;
            CreatePacket(0x0B, 0x15, 0x03, 4, buf);
            _ProcessStep = _ProcessMax;
            return true;
        }
        return getResult(false, oEvent);
    }

    /**
     * 关闭句柄返回结果
     * @param oEvent
     * @param model
     * @return
     */
    private boolean closeFileResult(INConnectorEvent oEvent, Door8800PacketModel model) {
        if (CheckResponse_Cmd(model, 0x0B, 0x15, 0x03)) {
            long crc = ByteUtil.CreateCRC32(mResult.FileDatas, 0, mResult.FileSize);
            mResult.Result=crc == mResult.FileCRC;
            return getResult(mResult.Result, oEvent);
        }
        return false;
    }

    /**
     * 获取返回值
     * @param result
     * @param oEvent
     * @return
     */
    private boolean getResult(boolean result, INConnectorEvent oEvent) {
        RaiseCommandCompleteEvent(oEvent);
        return result;
    }

    @Override
    protected void Release0() {

    }
}

package Face.AdditionalData;

import Door.Access.Connector.INConnectorEvent;
import Door.Access.Door8800.Command.Door8800Command;
import Door.Access.Door8800.Packet.Door8800PacketModel;
import Door.Access.Util.ByteUtil;
import Face.AdditionalData.Parameter.ReadFeatureCode_Parameter;
import Face.AdditionalData.Result.ReadFeatureCode_Result;
import io.netty.buffer.ByteBuf;

/**
 * 读取指纹
 *
 * @author F
 */
public class ReadFeatureCode extends Door8800Command {

    /**
     * 命令分类
     */
    int CMD_TYPE = 0X0B;
    /**
     * 命令
     */
    int CMD_INDEX = 0X05;
    /**
     * 数据
     */
    private byte[] _FileDatas;
    /**
     * 返回值
     */
    ReadFeatureCode_Result mResult;
    /**
     * 参数
     */
    ReadFeatureCode_Parameter mPar;

    /**
     * @param parameter
     */
    public ReadFeatureCode(ReadFeatureCode_Parameter parameter) {
        _Parameter = parameter;
        mPar = parameter;
        ByteBuf buf = ByteUtil.ALLOCATOR.buffer(6);
        parameter.getBytes(buf);
        CreatePacket(0x0b, 0x05, 0x00, 6, buf);
    }

    @Override
    protected boolean _CommandStep(INConnectorEvent oEvent, Door8800PacketModel model) {
        /**
         * 返回句柄
         */
        if (CheckResponse_Cmd(model, CMD_TYPE, CMD_INDEX, 0x01)) {
            return CheckFileHandle(oEvent, model);
        }
        /**
         * 返回文件内容
         */
        if (CheckResponse_Cmd(model, CMD_TYPE, CMD_INDEX, 0x02)) {
            return ReadFile(oEvent, model);
        }
        /**
         * 返回CRC32校验码
         */
        if (CheckResponse_Cmd(model, CMD_TYPE, CMD_INDEX, 0x03)) {
            return CheckCrc32(oEvent, model);
        }
        return false;
    }

    /**
     * 检查句柄
     *
     * @param oEvent
     * @param model
     */
    private boolean CheckFileHandle(INConnectorEvent oEvent, Door8800PacketModel model) {
        if (mResult == null) {
            mResult = new ReadFeatureCode_Result();
            _Result = mResult;
        }
        mResult.setBytes(model.GetDatabuff());
        if (mResult.UserCode != mPar.UserCode || mResult.FileType != mPar.Type
                || mResult.FileHandle == 0 || mResult.FileSize == 0) {
            mResult.Result = false;
            
            RaiseCommandCompleteEvent(oEvent);
            return true;
        }
        _FileDatas = new byte[mResult.FileSize];
        _ProcessMax = mResult.FileSize;
        CommandWaitResponse();
        return true;
    }

    /**
     * 读取文件
     */
    private boolean ReadFile(INConnectorEvent oEvent, Door8800PacketModel model) {
        ByteBuf buf = model.GetDatabuff();
        int FileHandle;
        int iDataIndex;
        int iSize = 0;
        iSize = ((int) model.GetDataLen()) - 7;
        FileHandle = buf.readInt();
        iDataIndex = buf.readMedium();
        _ProcessStep = iDataIndex + iSize;
        buf.readBytes(_FileDatas, iDataIndex, iSize);
        CommandWaitResponse();
        return true;
    }

    /**
     * CRC32校验
     */
    private boolean CheckCrc32(INConnectorEvent oEvent, Door8800PacketModel model) {

        long fileCrc32 = model.GetDatabuff().readUnsignedInt();
        long crc32 = ByteUtil.CreateCRC32(_FileDatas, 0, _FileDatas.length);
        mResult.FileCRC = fileCrc32;
        _ProcessStep = _ProcessMax;

        if (crc32 == fileCrc32) {
            mResult.FileDatas = _FileDatas;
            mResult.Result = true;
        }
        // _FileDatas = null;
        
        RaiseCommandCompleteEvent(oEvent);
        return mResult.Result;
    }

    @Override
    protected void Release0() {

    }
}

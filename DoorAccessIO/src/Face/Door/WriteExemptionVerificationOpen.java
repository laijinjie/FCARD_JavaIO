package Face.Door;

import Door.Access.Door8800.Command.Door8800Command;
import Door.Access.Util.ByteUtil;
import Face.Door.Parameter.ExemptionVerificationOpen_Parameter;
import io.netty.buffer.ByteBuf;

/**
 * 免验证开门_写入
 * @author F
 */
public class WriteExemptionVerificationOpen extends Door8800Command {
    int DATA_LEN = 3;
/**
 * 免验证开门_写入
 * @param par 免验证开门_写入参数
 */
    public WriteExemptionVerificationOpen(ExemptionVerificationOpen_Parameter par) {
        _Parameter = par;
        CreatePacket(0x03, 0x011, 0x00, DATA_LEN, getDataBuf(par));
    }

    private ByteBuf getDataBuf(ExemptionVerificationOpen_Parameter par) {
        ByteBuf buf = ByteUtil.ALLOCATOR.buffer(DATA_LEN);
        buf.writeBoolean(par.IsUseExemptionVerification);
        buf.writeBoolean(par.IsUseAutomaticRegistration);
        buf.writeByte((par.PeriodNumber));
        return buf;
    }

    @Override
    protected void Release0() {

    }
}

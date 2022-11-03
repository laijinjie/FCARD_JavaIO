package Face.System.Parameter;

import Door.Access.Command.CommandDetail;
import Door.Access.Command.CommandParameter;
import Face.Data.OEMDetail;

/**
 * 写入OEM参数
 */
public class WriteOEM_Parameter extends CommandParameter {
    /**
     * OEM详情信息
     */
    public OEMDetail oemDetail;

    /**
     * 写入OEM参数
     * @param detail 连接命令
     * @param oemDetail OEM信息详情
     */
    public WriteOEM_Parameter(CommandDetail detail, OEMDetail oemDetail) {
        super(detail);
        this.oemDetail = oemDetail;
        CheckParameter();
    }

    /**
     * 数据包长度（内部使用）
     * @return
     */
    public int getDataLen() {
        return oemDetail.getDataLen();
    }

    /**
     * 检查参数
     */
    private void CheckParameter() {
        if (oemDetail.DeliveryDate == null) {
            throw new UnsupportedOperationException("oemDetail.DeliveryDate is null");
        }
        if (oemDetail.Manufacturer.length() > 60) {
            throw new UnsupportedOperationException("oemDetail.Manufacturer  Length over limit (60)");
        }
        if (oemDetail.WebAddress.length() > 60) {
            throw new UnsupportedOperationException("oemDetail.WebAddress Length over limit (60)");
        }
    }
}

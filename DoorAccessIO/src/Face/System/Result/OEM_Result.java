package Face.System.Result;

import Door.Access.Command.INCommandResult;
import Face.Data.OEMDetail;

/**
 * OEM返回结果
 */
public class OEM_Result implements INCommandResult {
    /**
     * OEM信息
     */
    public OEMDetail detail;

    /**
     * OEM返回结果
     * @param detail OEM信息
     */
    public OEM_Result(OEMDetail detail) {
        this.detail = detail;
    }

    @Override
    public void release() {

    }
}

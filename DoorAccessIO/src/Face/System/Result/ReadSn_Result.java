package Face.System.Result;

import Door.Access.Command.INCommandResult;

/**
 * 读取SN命令返回结果
 */
public class ReadSn_Result implements INCommandResult {
    /**
     * 设备SN
     */
    public  String Sn;

    /**
     * 初始化返回结果
     * @param sn 设备sn
     */
    public ReadSn_Result(String sn){
        Sn=sn;
    }

    @Override
    public void release() {

    }
}

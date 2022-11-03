package Face.Door.Result;

import Door.Access.Command.INCommandResult;

/**
 *读卡器字节数_结果
 */
public class ReaderOption_Result implements INCommandResult {
    /**
     * 门字节数组<br>
     * 1 - 韦根26(三字节)
     * 2 - 韦根34(四字节)
     * 3 - 韦根26(二字节)
     * 4 - 禁用
     * 5 - 8字节
     */
    public byte ReaderOption;

    @Override
    public void release() {

    }
}

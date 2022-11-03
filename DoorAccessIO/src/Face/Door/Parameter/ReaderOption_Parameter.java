package Face.Door.Parameter;

import Door.Access.Command.CommandDetail;
import Door.Access.Command.CommandParameter;


/**
 * 读卡器字节数_参数
 *
 * @author F
 */
public class ReaderOption_Parameter extends CommandParameter {

    /**
     * 门字节数组<br>
     * 1 - 韦根26(三字节)<br>
     * 2 - 韦根34(四字节)<br>
     * 3 - 韦根26(二字节)<br>
     * 4 - 禁用<br>
     * 5 - 8字节
     */
    public byte ReaderOption;
    /**
     * 最大取值范围
     */
    final int MAX_VALUE = 5;
    /**
     * 最小取值范围
     */
    final int MIN_VALUE = 1;

    /**
     * 构造方法
     *
     * @param detail       命令详情
     * @param readerOption 门字节数组<br> 1 - 韦根26(三字节)<br> 2 - 韦根34(四字节)<br>3 - 韦根26(二字节)<br>4 - 禁用<br>5 - 8字节
     */
    public ReaderOption_Parameter(CommandDetail detail, byte readerOption) {
        super(detail);
        ReaderOption = readerOption;
        CheckedParameter();
    }

    private boolean CheckedParameter() {
        if (ReaderOption < MIN_VALUE || ReaderOption > MAX_VALUE) {
            throw new UnsupportedOperationException(String.format("ReaderOption must between 1 and 5!", MIN_VALUE, MAX_VALUE));
        }
        return true;
    }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Door.Access.Door8800.Command.Data;

import io.netty.buffer.ByteBuf;
import java.util.Calendar;

/**
 *
 * @author FCARD
 */
public class HolidayDetail {

    /**
     * 节假日的索引号
     */
    public byte Index;
    /**
     * 节假日日期
     */
    public Calendar Holiday;
    /**
     * 节假日类型 1、上午 (00:00:00 - 11:59:59)<br>
     * 2、下午 (12:00:00 - 23:59:59)<br>
     * 3、全天 (00:00:00 - 23:59:59)
     */
    public byte HolidayType;
    /**
     * 表示，是否每年循环
     */
    public boolean YearLoop;
    
}

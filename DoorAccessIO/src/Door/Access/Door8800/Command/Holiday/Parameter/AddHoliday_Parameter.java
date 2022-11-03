/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Door.Access.Door8800.Command.Holiday.Parameter;

import Door.Access.Command.CommandDetail;
import Door.Access.Command.CommandParameter;
import Door.Access.Door8800.Command.Data.HolidayDetail;
import Door.Access.Util.ByteUtil;
import io.netty.buffer.ByteBuf;
import java.util.ArrayList;

/**
 *
 * @author FCARD
 */
public class AddHoliday_Parameter extends CommandParameter {

    ArrayList<HolidayDetail> Holidays;

    public AddHoliday_Parameter(CommandDetail detail, ArrayList<HolidayDetail> holidayDetails) throws Exception {
        super(detail);
        Holidays = holidayDetails;
        checkedParameter();
    }

    private void checkedParameter() throws Exception {
        if (Holidays == null) {
            throw new Exception("Holidays is null");
        }
        int size = Holidays.size();
        if (size > 30 || size == 0) {
            throw new Exception("Holidays Max Size value Range 1-30");
        }

        for (int i = 0; i < Holidays.size(); i++) {
            HolidayDetail holiday = Holidays.get(i);
            if (holiday.Index > 30 || holiday.Index == 0) {
                throw new Exception("holiday Index  value Range 1-30");
            }
            if (holiday.HolidayType < 1 || holiday.HolidayType > 3) {
                throw new Exception("HolidayType Error value Range 1-3");
            }
            if(holiday.Holiday==null){
                 throw new Exception("Holiday Error! is null");
            }
            if(!holiday.YearLoop){
                int year = holiday.Holiday.get(holiday.Holiday.YEAR);
                if (year > 2099 || year < 2000) {
                    throw new Exception("Year Error! value Range 2000-2099");
                }
            }            
        }

    }

    public ByteBuf GetBytes() {
        int size = Holidays.size();
        int len = (5 * size) + 4;
        ByteBuf buf = ByteUtil.ALLOCATOR.buffer(len);
        buf.writeInt(size);
        for (int i = 0; i < size; i++) {
            HolidayDetail holiday = Holidays.get(i);
            buf.writeByte(holiday.Index);
            if (holiday.YearLoop) {
                buf.writeByte(0);
            } else {
                int year = holiday.Holiday.get(holiday.Holiday.YEAR);
                buf.writeByte(ByteUtil.ByteToBCD((byte) (year - 2000)));
            }
            int month = holiday.Holiday.get(holiday.Holiday.MONTH)+1;
            int day = holiday.Holiday.get(holiday.Holiday.DATE);
            buf.writeByte(ByteUtil.ByteToBCD((byte) (month)));
            buf.writeByte(ByteUtil.ByteToBCD((byte) (day)));
            buf.writeByte(holiday.HolidayType);
        }
        return buf;
    }
}

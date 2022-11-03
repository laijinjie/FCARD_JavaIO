/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Door.Access.Door8800.Command.Holiday.Result;

import Door.Access.Command.INCommandResult;
import Door.Access.Door8800.Command.Data.HolidayDetail;
import Door.Access.Util.ByteUtil;
import io.netty.buffer.ByteBuf;
import java.util.ArrayList;
import java.util.Calendar;

/**
 *
 * @author FCARD
 */
public class ReadAllHoliday_Result implements INCommandResult {

    public ArrayList<HolidayDetail> Holidays;

    public ReadAllHoliday_Result() {
        Holidays = new ArrayList<HolidayDetail>();
    }

    @Override
    public void release() {

    }

    public void SetBytes(ByteBuf buf) {
        int count = buf.readInt();
        for (int i = 0; i < count; i++) {
            HolidayDetail detail = new HolidayDetail();
            detail.Index = buf.readByte();
            detail.Holiday = Calendar.getInstance();
            int iYear = buf.readByte();
            if (iYear == 0) {
                detail.YearLoop = true;
                iYear = detail.Holiday.get(Calendar.YEAR);
            } else {
                iYear = 2000 + ByteUtil.BCDToByte((byte) iYear);
            }
            int iMonth = ByteUtil.BCDToByte(buf.readByte());
            int iDay = ByteUtil.BCDToByte(buf.readByte());
            detail.Holiday.set(iYear, iMonth-1, iDay);
            detail.HolidayType = buf.readByte();
            Holidays.add(detail);
        }

    }
}

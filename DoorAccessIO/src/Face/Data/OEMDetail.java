package Face.Data;

import Door.Access.Util.ByteUtil;
import Door.Access.Util.StringUtil;
import Door.Access.Util.TimeUtil;
import io.netty.buffer.ByteBuf;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * oem信息
 */
public class OEMDetail {
    /**
     * 制造商名称
     */
    public String Manufacturer;
    /**
     * 网址
     */
    public String WebAddress;
    /**
     * 出厂日期
     */
    public Calendar DeliveryDate;

    public int getDataLen() {
        return 127;
    }

    public ByteBuf getBytes() throws UnsupportedEncodingException {
        ByteBuf buf = ByteUtil.ALLOCATOR.buffer(getDataLen());
        ByteUtil.writeString(buf, Manufacturer, 60);
        ByteUtil.writeString(buf, WebAddress, 60);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmss");
        StringUtil.HextoByteBuf(sdf.format(DeliveryDate.getTime()), buf);
        return buf;
    }

    public void setBytes(ByteBuf buf) throws UnsupportedEncodingException {
        Manufacturer = ByteUtil.getString(buf, 60);
        WebAddress = ByteUtil.getString(buf, 60);
        DeliveryDate = TimeUtil.BCDTimeToDate_yyyyMMddhhmmss(buf);
    }
}

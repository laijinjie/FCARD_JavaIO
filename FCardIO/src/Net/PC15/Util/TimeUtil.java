/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Net.PC15.Util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 *
 * @author 赖金杰
 */
public class TimeUtil {

    public static Calendar BCDTimeToDate_ssmmhhddMMyy(byte btTime[]) {
        btTime = ByteUtil.BCDToByte(btTime);
        int year = ByteUtil.uByte(btTime[5]);
        int month = ByteUtil.uByte(btTime[4]);
        int dayOfMonth = ByteUtil.uByte(btTime[3]);
        int hourOfDay = ByteUtil.uByte(btTime[2]);
        int minute = ByteUtil.uByte(btTime[1]);
        int second = ByteUtil.uByte(btTime[0]);

        if (year > 99) {
            return null;
        }
        if (month == 0 || month > 12) {
            return null;
        }
        if (dayOfMonth == 0 || dayOfMonth > 31) {
            return null;
        }
        if (hourOfDay > 23) {
            return null;
        }

        if (minute > 59) {
            return null;
        }

        if (second > 59) {
            return null;
        }

        Calendar dTime = new GregorianCalendar(2000 + year, month - 1, dayOfMonth, hourOfDay, minute, second);
        return dTime;
    }

    public static Calendar BCDTimeToDate_yyMMddhh(byte btTime[]) {
        btTime = ByteUtil.BCDToByte(btTime);
        int year = ByteUtil.uByte(btTime[0]);
        int month = ByteUtil.uByte(btTime[1]);
        int dayOfMonth = ByteUtil.uByte(btTime[2]);
        int hourOfDay = ByteUtil.uByte(btTime[3]);

        if (year > 99) {
            return null;
        }
        if (month == 0 || month > 12) {
            return null;
        }
        if (dayOfMonth == 0 || dayOfMonth > 31) {
            return null;
        }
        if (hourOfDay > 23) {
            return null;
        }

        Calendar dTime = new GregorianCalendar(2000 + year, month - 1, dayOfMonth, hourOfDay, 0, 0);
        return dTime;
    }

    public static void DateToBCD_yyMMddhh(byte[] btData, Calendar date) {
        if (date == null) {
            for (int i = 0; i < 4; i++) {
                btData[i] = 0;
            }
        } else {
            btData[0] = (byte) (date.get(Calendar.YEAR) - 2000);
            btData[1] = (byte) (date.get(Calendar.MONTH) + 1);
            btData[2] = (byte) date.get(Calendar.DAY_OF_MONTH);
            btData[3] = (byte) date.get(Calendar.HOUR_OF_DAY);
            btData = ByteUtil.ByteToBCD(btData);
        }
    }

    public static void DateToBCD_ssmmhhddMMwwyy(byte[] btData, Calendar date) {
        if (date == null) {
            for (int i = 0; i < 7; i++) {
                btData[i] = 0;
            }
        } else {
            int MONTH = date.get(Calendar.DAY_OF_MONTH) - 1;//这里获取到的周，周日表示1，周一表示2 ... 周六表示 7
            if (MONTH == 0) {
                MONTH = 7;
            }
            btData[6] = (byte) (date.get(Calendar.YEAR) - 2000);
            btData[5] = (byte) MONTH;
            btData[4] = (byte) (date.get(Calendar.MONTH) + 1);
            btData[3] = (byte) date.get(Calendar.DAY_OF_MONTH);
            btData[2] = (byte) date.get(Calendar.HOUR_OF_DAY);
            btData[1] = (byte) date.get(Calendar.MINUTE);
            btData[0] = (byte) date.get(Calendar.SECOND);
            btData = ByteUtil.ByteToBCD(btData);
        }
    }

    public static Calendar BCDTimeToDate_yyMMddhhmm(byte btTime[]) {
        btTime = ByteUtil.BCDToByte(btTime);
        int year = ByteUtil.uByte(btTime[0]);
        int month = ByteUtil.uByte(btTime[1]);
        int dayOfMonth = ByteUtil.uByte(btTime[2]);
        int hourOfDay = ByteUtil.uByte(btTime[3]);
        int minute = ByteUtil.uByte(btTime[4]);

        if (year > 99) {
            return null;
        }
        if (month == 0 || month > 12) {
            return null;
        }
        if (dayOfMonth == 0 || dayOfMonth > 31) {
            return null;
        }
        if (hourOfDay > 23) {
            return null;
        }

        if (minute > 59) {
            return null;
        }

        Calendar dTime = new GregorianCalendar(2000 + year, month - 1, dayOfMonth, hourOfDay, minute, 0);
        return dTime;
    }

    public static void DateToBCD_yyMMddhhmm(byte[] btData, Calendar date) {
        if (date == null) {
            for (int i = 0; i < 5; i++) {
                btData[i] = 0;
            }
        } else {
            btData[0] = (byte) (date.get(Calendar.YEAR) - 2000);
            btData[1] = (byte) (date.get(Calendar.MONTH) + 1);
            btData[2] = (byte) date.get(Calendar.DAY_OF_MONTH);
            btData[3] = (byte) date.get(Calendar.HOUR_OF_DAY);
            btData[4] = (byte) date.get(Calendar.MINUTE);
            btData = ByteUtil.ByteToBCD(btData);
        }
    }

    public static Calendar BCDTimeToDate_yyMMddhhmmss(byte btTime[]) {
        btTime = ByteUtil.BCDToByte(btTime);
        int year = ByteUtil.uByte(btTime[0]);
        int month = ByteUtil.uByte(btTime[1]);
        int dayOfMonth = ByteUtil.uByte(btTime[2]);
        int hourOfDay = ByteUtil.uByte(btTime[3]);
        int minute = ByteUtil.uByte(btTime[4]);
        int second = ByteUtil.uByte(btTime[5]);

        if (year > 99) {
            return null;
        }
        if (month == 0 || month > 12) {
            return null;
        }
        if (dayOfMonth == 0 || dayOfMonth > 31) {
            return null;
        }
        if (hourOfDay > 23) {
            return null;
        }

        if (minute > 59) {
            return null;
        }
        if (second > 59) {
            return null;
        }

        Calendar dTime = new GregorianCalendar(2000 + year, month - 1, dayOfMonth, hourOfDay, minute, second);
        return dTime;
    }

    public static void DateToBCD_yyMMddhhmmss(byte[] btData, Calendar date) {
        if (date == null) {
            for (int i = 0; i < 6; i++) {
                btData[i] = 0;
            }
        } else {
            btData[0] = (byte) (date.get(Calendar.YEAR) - 2000);
            btData[1] = (byte) (date.get(Calendar.MONTH) + 1);
            btData[2] = (byte) date.get(Calendar.DAY_OF_MONTH);
            btData[3] = (byte) date.get(Calendar.HOUR_OF_DAY);
            btData[4] = (byte) date.get(Calendar.MINUTE);
            btData[5] = (byte) date.get(Calendar.SECOND);
            btData = ByteUtil.ByteToBCD(btData);
        }
    }

    /**
     * 将时间类型格式化为 yyyy-MM-dd HH:mm:ss
     *
     * @param date 需要格式化的时间
     * @return 时间字符串
     */
    public static String FormatTime(Calendar date) {
        if (date == null) {
            return "";
        }
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.format(date.getTime());
    }

    /**
     * 将时间类型格式化为 yyyy-MM-dd HH:mm
     *
     * @param date 需要格式化的时间
     * @return 时间字符串
     */
    public static String FormatTimeHHmm(Calendar date) {
        if (date == null) {
            return "";
        }
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return format.format(date.getTime());
    }

    public static String FormatTimeMillisecond(Calendar date) {
        if (date == null) {
            return "";
        }
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss.sss");
        return format.format(date.getTime());
    }
}

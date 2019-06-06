/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Net.PC15.Util;

import io.netty.buffer.ByteBuf;
import java.lang.StringBuilder;
import java.util.Random;

/**
 * 十六进制实用工具类
 *
 * @author 赖金杰
 */
public class StringUtil {

    private final static byte[] HexToByte_Digit;
    private final static byte[] NumDigit;

    //初始化 HexToByte 的转换表
    static {
        int i;
        byte[] digits = new byte[256];
        byte[] tmp;
        tmp = "0123456789abcdef".getBytes();
        for (i = 0; i < tmp.length; i++) {
            digits[tmp[i]] = (byte) i;
        }
        tmp = "ABCDEF".getBytes();
        for (i = 0; i < tmp.length; i++) {
            digits[tmp[i]] = (byte) (i + 10);
        }
        HexToByte_Digit = digits;

        digits = new byte[256];
        tmp = "0123456789".getBytes();
        for (i = 0; i < tmp.length; i++) {
            digits[tmp[i]] = (byte) i;
        }

        NumDigit = digits;

    }

    /**
     * 十六进制字符串转字节数组
     *
     * @param hexString 需要转换的十六进制字符串
     * @return 转换后的字节数组 如果字符串为null则返回null
     */
    public static byte[] HexToByte(String hexString) {
        int i, iIndex = 0, iData;
        if (IsNullOrEmpty(hexString)) {
            return null;
        }
        if (!IsHex(hexString)) {
            return null;
        }

        //确定字符串长度必须是2的倍数
        if ((hexString.length() % 2) == 1) {
            hexString = "0" + hexString;
        }
        //生成转换值列表
        byte[] digits = HexToByte_Digit;

        //生成缓存
        byte[] buf = new byte[hexString.length() / 2];
        byte[] sbuf = hexString.getBytes();
        int ilen = sbuf.length;
        //开始转换
        for (i = 0; i < ilen; i++) {
            iData = digits[sbuf[i++] & 0x000000ff] * 16;
            iData = iData + digits[sbuf[i]];

            buf[iIndex] = (byte) iData;
            iIndex++;
        }

        return buf;
    }

    /**
     * 将十六进制字符串添加到ByteBuf中
     *
     * @param hexString
     * @param buf
     */
    public static void HextoByteBuf(String hexString, ByteBuf buf) {
        int i, iIndex = 0, iData;
        if (IsNullOrEmpty(hexString)) {
            return;
        }
        if (!IsHex(hexString)) {
            return;
        }

        //确定字符串长度必须是2的倍数
        if ((hexString.length() % 2) == 1) {
            hexString = "0" + hexString;
        }
        //生成转换值列表
        byte[] digits = HexToByte_Digit;

        //生成缓存
        byte[] sbuf = hexString.getBytes();
        int ilen = sbuf.length;
        //开始转换
        for (i = 0; i < ilen; i++) {
            iData = digits[sbuf[i++] & 0x000000ff] * 16;
            iData = iData + digits[sbuf[i]];

            buf.writeByte((byte) iData);
            iIndex++;
        }
    }

    /**
     * 检查是否为十六进制字符串
     *
     * @param hexString 需要检查的字符串
     * @return true 表示是十六进制，false 表示包含非十六进制字符
     */
    public static boolean IsHex(String hexString) {
        if (IsNullOrEmpty(hexString)) {
            return false;
        }
        byte[] sbuf = hexString.getBytes();
        byte[] digits = HexToByte_Digit;
        int i, ilen = sbuf.length;
        //开始转换
        for (i = 0; i < ilen; i++) {
            if (digits[sbuf[i] & 0x000000ff] == 0 && sbuf[i] != 0x30) {
                return false;
            }
        }
        return true;
    }

    /**
     * 检查是否有纯数字组成
     *
     * @param numString 需要检查的字符串
     * @return true 表示是纯数字字符串；false 包含非法字符
     */
    public static boolean IsNum(String numString) {
        if (IsNullOrEmpty(numString)) {
            return false;
        }
        byte[] sbuf = numString.getBytes();
        byte[] digits = NumDigit;
        int i, ilen = sbuf.length;
        //开始转换
        for (i = 0; i < ilen; i++) {

            if (digits[sbuf[i] & 0x000000ff] == 0 && sbuf[i] != 0x30) {
                return false;
            }
        }
        return true;
    }

    /**
     * 检查字符串是否为空字符串或者为null
     *
     * @param str 需要检查的字符串
     * @return true表示为空或null
     */
    public static boolean IsNullOrEmpty(String str) {
        if (str == null) {
            return true;
        }
        if (str.length() == 0) {
            return true;
        }
        return false;
    }

    /**
     * 检查字符串是否为Ascii字符
     *
     * @param asciiString 待检查的字符串
     * @return true 表示都是ascii组成，false 表示有包含不是ascii的字符串
     */
    public static boolean IsAscii(String asciiString) {
        if (IsNullOrEmpty(asciiString)) {
            return false;
        }
        byte[] buf = asciiString.getBytes();
        int ilen = buf.length;
        for (int i = 0; i < ilen; i++) {
            if (buf[i] < 32 || buf[i] > 125) {
                return false;
            }
        }
        return true;
    }

    /**
     * 填充字符串，并返回一个指定长度的字符串，原始字符串长度大于指定长度时将被阶段，小于指定长度时，使用 fillstr 参数填充内容
     * 填充的字符串在右边
     *
     * @param str
     * @param iLen
     * @param fillstr
     * @return
     */
    public static String FillString(String str, int iLen, String fillstr) {
        return FillString(str, iLen, fillstr, true);
    }

    /**
     * 填充字符串，并返回一个指定长度的字符串，原始字符串长度大于指定长度时将被阶段，小于指定长度时，使用 fillstr 参数填充内容
     *
     * @param str
     * @param iLen
     * @param fillstr
     * @param fill_right 填充值在右边
     * @return
     */
    public static String FillString(String str, int iLen, String fillstr, boolean fill_right) {
        int iStrLen = 0;

        if (!IsNullOrEmpty(str)) {
            iStrLen = str.length();

            if (iStrLen == iLen) {
                return str;
            }
        }

        if (iStrLen > iLen) {
            if (fill_right) {
                return str.substring(0, iLen);
            } else {
                return str.substring(iStrLen - iLen, iStrLen);
            }

        }
        StringBuilder sbuf = new StringBuilder(iLen);

        int iAddCount = iLen - iStrLen;
        for (int i = 0; i < iAddCount; i++) {
            sbuf.append(fillstr);
            if (sbuf.length() > iAddCount) {
                break;
            }
        }
        if (sbuf.length() > iAddCount) {
            sbuf.setLength(iAddCount);
        }
        if (!IsNullOrEmpty(str)) {
            if (fill_right) {
                sbuf.insert(0, str);
            } else {
                sbuf.append(str);
            }

        }

        return sbuf.toString();
    }

    /**
     * 检查并填充十六进制字符串
     *
     * @param str 需要检查的字符串
     * @param iLen 需要返回的字符串长度
     * @param fillstr 占位字符
     * @param fill_right 填充在结尾还是开头？ True 表示填充在结尾。
     * @return
     */
    public static String FillHexString(String str, int iLen, String fillstr, boolean fill_right) {
        StringBuilder sbuf = new StringBuilder(iLen);

        if (StringUtil.IsNullOrEmpty(str)) {
            for (int i = 0; i < iLen; i++) {
                sbuf.append(fillstr);
            }
            sbuf.setLength(iLen);
            return sbuf.toString();
        }
        if (!StringUtil.IsHex(str)) {
            for (int i = 0; i < iLen; i++) {
                sbuf.append(fillstr);
            }
            sbuf.setLength(iLen);
            return sbuf.toString();
        }

        return FillString(str, iLen, fillstr, fill_right);

    }
    
    public static String[] LongToString(long longArray[]) {
         if (longArray == null || longArray.length < 1) {
             return null;
         }
         String  stringArray[] = new String[longArray.length];
         for (int i = 0; i < stringArray.length; i++ ) {
             try {
              stringArray[i] = String.valueOf(longArray[i]);
             } catch (NumberFormatException e) {
              stringArray[i] = null;
                 continue;
             }
         }
         return stringArray;
     }
    
        /**
        * 16进制直接转换成为字符串(无需Unicode解码)
        * @param hexStr
        * @return
        */
       public static String HexStr2Str(String hexStr,int i) {
           Integer x = Integer.parseInt(hexStr,i);
           return String.valueOf(x);
       }
       
       /**
        * 字符串转化成为16进制字符串
        * @param s
        * @return
        */
       public static String StrTo16(String s) {
           String str = "";
           for (int i = 0; i < s.length(); i++) {
               int ch = (int) s.charAt(i);
               String s4 = Integer.toHexString(ch);
               str = str + s4;
           }
           return str;
       }
       
       /**
	 * 获取16进制随机数
	 * @param len
	 * @return
	 * @throws CoderException
	 */
	public static String randomHexString(int len)  {
		try {
			StringBuffer result = new StringBuffer();
			for(int i=0;i<len;i++) {
				result.append(Integer.toHexString(new Random().nextInt(16)));
			}
			return result.toString().toUpperCase();
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			
		}
		return null;
		
	}
        
        /**
	 * 仿照String的trim()
	 * @param strArg：需要进行去掉前后空格符的字符串
	 * @return：返回字符串的副本，该副本进行去掉了首尾空格符
	 */
	public static String LTrim(String strArg,char text){
		
		char[] cVal=strArg.toCharArray();
		int p1=0;
		int len=cVal.length;
		
		//从首到尾进行遍历，如果发现了第一个不是  ' ' 就break:表示终止了遍历，找到了首部到尾部第一个不为 ' ' 的位置
		while(p1 < len){
			if (cVal[p1] == text) {
				p1 += 1;
			}else{
				break;
			}
		}
		
		//这说明  strArg 压根就是由空格字符组成的字符串
		if (p1 == len) {
			return "";
		}
		
		//从尾部到首部进行遍历，如果发现了第一个不是  ' ' 就break:表示终止了遍历，找到了尾部到首部第一个不为 ' ' 的位置
		int p2=len-1;
                /*
		while(p2 >= 0){
			if (cVal[p2] == ' ') {
				p2 -= 1;
			}else{
				break;
			}
		}
		*/
		String subStr=strArg.substring(p1,p2+1);
		return subStr;
	}
}

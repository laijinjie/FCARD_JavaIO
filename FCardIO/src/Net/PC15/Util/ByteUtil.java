/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Net.PC15.Util;

import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.buffer.UnpooledByteBufAllocator;
import io.netty.util.NettyRuntime;
import io.netty.util.internal.PlatformDependent;
import io.netty.util.internal.SystemPropertyUtil;

/**
 *
 * @author 赖金杰
 */
public class ByteUtil {

    /**
     * 默认的 ByteBuf 非池化的堆内存。
     */
    
    public static final ByteBufAllocator UNPOOLED_HEAP_ALLOCATOR
            = new UnpooledByteBufAllocator(false);

    /**
     * 默认的 ByteBuf 分配器，使用非池化的堆内存。
     */
    public static final ByteBufAllocator ALLOCATOR
            = UNPOOLED_HEAP_ALLOCATOR;
    private final static char[] ByteToHex_Digit = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

    /**
     * Uint8 的最大值,0xFFFFFFFF
     */
    public static final short UINT8_MAX = 255;

    /**
     * Uint8 的最小值,0
     */
    public static final long UINT8_MIn = 0L;

    /**
     * 检查一个数值，是否为UINT8的值
     *
     * @param num
     * @return
     */
    public static boolean Check(short num) {
        if (num > UINT8_MAX || num < UINT8_MIn) {
            return false;
        }
        return true;
    }

    /**
     * 获取无符号的Byte值
     *
     * @param byte0 需要转换的byte
     * @return 返回一个int型，表示byte的无符号数据
     */
    public static int uByte(byte byte0) {
        return byte0 & 0x000000ff;
    }

    /**
     * Byte到Hex的转换
     *
     * @param b 需要转换的字节数组，转换的长度
     * @return 十六进制字符串
     */
    public static String ByteToHex(byte[] b) {
        if (b == null) {
            return null;
        }
        if (b.length == 0) {
            return null;
        }

        int ilen = b.length;
        char[] sHexbuf = new char[ilen * 2];
        int lIndex = 0;
        int iData;
        char[] digits = ByteToHex_Digit;
        try {
            for (int i = 0; i < ilen; i++) {
                iData = uByte(b[i]);//取字节的无符号整形数值

                sHexbuf[lIndex++] = digits[iData / 16];
                sHexbuf[lIndex++] = digits[iData % 16];
            }
            return new String(sHexbuf).toUpperCase();
        } catch (Exception e) {
            return new String();
        }
    }

    /**
     * 显示字节数组中的内容
     *
     * @param bBytes 需要显示的byte数组
     * @return 返回连接了所有元素的字符串
     */
    public static String BytesToString(byte bBytes[]) {
        if (bBytes == null || bBytes.length == 0) {
            return null;
        }
        int ilen = bBytes.length;
        StringBuilder buf = new StringBuilder(ilen * 5);
        int ret;
        int iRowCount = 0;
        for (int i = 0; i < bBytes.length; i++) {
            ret = uByte(bBytes[i]);//取字节的无符号整形数值
            buf.append(ret);
            buf.append(",");
            iRowCount++;
            if (iRowCount == 8) {
                buf.append("\n");
                iRowCount = 0;
            }
        }
        buf.setLength(buf.length() - 1);
        return buf.toString();
    }

    public static byte BoolToByte(boolean b) {
        if (b) {
            return 1;
        } else {
            return 0;
        }

    }

    /**
     * BCD 转 字节
     *
     * @param iNum
     * @return
     */
    public static byte BCDToByte(byte iNum) {
        int iValue = uByte(iNum);
        iValue = ((iValue / 16) * 10) + (iValue % 16);
        return (byte) iValue;
    }

    /**
     * BCD 转 字节
     *
     * @param iNum
     * @return
     */
    public static byte[] BCDToByte(byte iNum[]) {
        int iLen = iNum.length;
        for (int i = 0; i < iLen; i++) {
            iNum[i] = BCDToByte(iNum[i]);
        }
        return iNum;
    }

    /**
     * 字节转BCD码
     *
     * @param iNum
     * @return
     */
    public static byte ByteToBCD(byte iNum) {
        int iValue = uByte(iNum);
        iValue = (iValue / 10) * 16 + (iValue % 10);
        return (byte) iValue;
    }

    /**
     * 字节转BCD码
     *
     * @param iNum
     * @return
     */
    public static byte[] ByteToBCD(byte iNum[]) {
        int iLen = iNum.length;
        for (int i = 0; i < iLen; i++) {
            iNum[i] = ByteToBCD(iNum[i]);
        }
        return iNum;
    }

}

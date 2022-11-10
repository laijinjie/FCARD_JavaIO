/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Door.Access.Util;

import io.netty.buffer.ByteBuf;
import java.util.Random;

/**
 *
 * @author 赖金杰
 */
public class UInt32Util {

    /**
     * 内置的随机数种子
     */
    private static final Random DEFAULT_Random = new Random(System.currentTimeMillis());

    /**
     * Uint32 的最大值,0xFFFFFFFF
     */
    public static final long UINT32_MAX = 4294967295L;

    /**
     * Uint32 的最小值,0
     */
    public static final long UINT32_MIn = 0L;

    /**
     * 检查一个数值，是否为UINT32的值
     *
     * @param num
     * @return
     */
    public static boolean Check(long num) {
        if (num > UINT32_MAX || num < UINT32_MIn) {
            return false;
        }
        return true;
    }

    /**
     * 返回一个整形的Uint32值
     *
     * @param num
     * @return
     */
    public static long UInt32(int num) {
        return (long) num & 0x00000000FFFFFFFFL;
    }

    /**
     * 返回一个整形的Uint32值
     *
     * @param num
     * @return
     */
    public static long UInt32(long num) {
        return num & 0x00000000FFFFFFFFL;
    }

    /**
     * 返回一个取值范围在0xFFFFFFE-0x1000 之间的数值
     *
     * @return
     */
    public static long GetRndNum() {
        long ivalue = (long) (DEFAULT_Random.nextDouble() * UINT32_MAX);
        return ivalue + 4096L;
    }

    public static ByteBuf UINT32ToByteBuf(long num) {
        ByteBuf buf = ByteUtil.ALLOCATOR.buffer(4);
        buf.writeInt((int) num);
        return buf;
    }

    /**
     * 检查数值是否在指定范围内
     *
     * @param Num 待检查的数值
     * @param iMin 最小数值
     * @param iMax 最大数值
     * @return
     */
    public static boolean CheckNum(int Num, int iMin, int iMax) {
        if (Num < iMin) {
            return false;
        }

        if (Num > iMax) {
            return false;
        }

        return true;
    }

    public static String ToHex(int value, int iLen) {
        String sHex = Integer.toHexString(value);
        int iHexLen = sHex.length();
        if (iHexLen > iLen) {
            return sHex.substring(iHexLen-iLen-1, iHexLen);
        }
        if (iHexLen == iLen) {
            return sHex;
        }
        if (iHexLen < iLen) {
            StringBuilder sHexbuf = new StringBuilder(iLen);
            int iAddCount = iLen-iHexLen;
            for (int i = 0; i < iAddCount; i++) {
                sHexbuf.append("0");
            }
            sHexbuf.append(sHex);
            return sHexbuf.toString();
        }
        return sHex;
    }

}

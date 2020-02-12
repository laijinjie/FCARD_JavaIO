/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Door.Access.Door89H.Command.Data;

import Door.Access.Util.ByteUtil;
import Door.Access.Util.StringUtil;
import Door.Access.Util.TimeUtil;
import io.netty.buffer.ByteBuf;
import java.math.BigInteger;
import java.util.ArrayList;

/**
 * 针对Door89H使用，卡片权限详情
 *
 * @author 徐铭康
 */
public class CardDetail extends Door.Access.Door8800.Command.Data.CardDetail {

    public CardDetail() {
        super();
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Door.Access.Door89H.Command.Data.CardDetail) {
            return compareTo((Door.Access.Door89H.Command.Data.CardDetail) o) == 0;
        } else {
            return false;
        }

    }

    @Override
    public int GetDataLen() {
        return 0x25;//37字节
    }

    @Override
    public void SetCardData(String value) throws Exception {
        if (value == null || value.length() == 0) {
            throw new Exception("卡号不能为空");
        }

        if (!StringUtil.IsNum(value)) {
            throw new Exception("卡号不是数字");
        }

        if (value.length() > 22) {
            throw new Exception("卡号长度超过22位数字");
        }

        String maxHex = new BigInteger(value, 10).toString(16);
        BigInteger biLongMax = new BigInteger("4722366482869645213695");
        BigInteger biCardData = new BigInteger(value);

        if (biLongMax.compareTo(biCardData) <= 0) {
            throw new Exception("卡号超过最大值");
        }

        CardData = value;
    }

    public void SetCardDataHEX(String value) throws Exception {
        if (value == null || value.length() == 0) {
            throw new Exception("卡号不能为空");
        }

        if (!StringUtil.IsHex(value)) {
            throw new Exception("卡号不是十六进制格式");
        }

        if (value.length() > 18) {
            throw new Exception("卡号长度超过9字节");
        }

        BigInteger num = new BigInteger(value, 16);

        CardData = num.toString();
    }

    @Override
    public void WriteCardData(ByteBuf data) throws Exception {
        byte[] buf = StringUtil.StringNumto9Byte(CardData);
        data.writeBytes(buf);
    }

    @Override
    public void ReadCardData(ByteBuf data) {
        byte[] btCardData = new byte[9];
        data.readBytes(btCardData, 0, 9);
        
        BigInteger card = new BigInteger(btCardData);

        CardData = card.toString();
    }
}

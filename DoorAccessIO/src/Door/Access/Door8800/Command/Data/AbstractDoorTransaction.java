/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Door.Access.Door8800.Command.Data;

import Door.Access.Data.AbstractTransaction;
import Door.Access.Util.ByteUtil;
import Door.Access.Util.TimeUtil;
import io.netty.buffer.ByteBuf;

/**
 * 关于门的事件抽象类
 *
 * @author 赖金杰
 */
public abstract class AbstractDoorTransaction extends AbstractTransaction {

    /**
     * 设定此事件的分类
     *
     * @param Type 分类
     */
    public AbstractDoorTransaction(int Type) {
        _TransactionType = (short) Type;
    }
    /**
     * 门号
     */
    public short Door;

    @Override
    public int GetDataLen() {
        return 8;
    }

    @Override
    public void SetBytes(ByteBuf data) {
        try {
            Door = data.readUnsignedByte();
            byte[] btTime = new byte[6];
            data.readBytes(btTime, 0, 6);

            if (ByteUtil.uByte(btTime[0]) == 255) {
                _IsNull = true;
                //return;
            }

            _TransactionDate = TimeUtil.BCDTimeToDate_yyMMddhhmmss(btTime);
            _TransactionCode = data.readUnsignedByte();
            if (_TransactionCode == 0) {
                _IsNull = true;
            }
        } catch (Exception e) {
        }

        return;
    }

    @Override
    public ByteBuf GetBytes() {
        return null;
    }
}

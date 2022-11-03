/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Door.Access.Door8800.Command.Password.Result;

import Door.Access.Command.INCommandResult;
import Door.Access.Door8800.Command.Data.PasswordDetail;
import Door.Access.Util.ByteUtil;
import io.netty.buffer.ByteBuf;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 *
 * @author F
 */
public class ReadPasswordDataBaseDetail_Result implements INCommandResult {

    /**
     * 密码容量
     */
    public int Capacity;
    /**
     * 已存数量
     */
    public int UseNumber;

    public void SetBytes(ByteBuf buf) {
        Capacity = buf.readUnsignedShort();
        UseNumber = buf.readUnsignedShort();
    }
    @Override
    public void release() {

    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Door.Access.Door8800.Command.Data;

import Door.Access.Data.INData;
import Door.Access.Util.ByteUtil;
import io.netty.buffer.ByteBuf;

/**
 * 记录数据库的详情<br>
 * 读卡记录,出门开关,门磁,远程开门,报警,系统记录
 *
 * @author 赖金杰
 */
public class TransactionDatabaseDetail implements INData {

    /**
     * 读卡相关记录
     */
    public TransactionDetail CardTransactionDetail;
    /**
     * 出门开关相关记录
     */
    public TransactionDetail ButtonTransactionDetail;
    /**
     * 门磁相关记录
     */
    public TransactionDetail DoorSensorTransactionDetail;
    /**
     * 远程操作相关记录
     */
    public TransactionDetail SoftwareTransactionDetail;
    /**
     * 报警相关记录
     */
    public TransactionDetail AlarmTransactionDetail;
    /**
     * 系统相关记录
     */
    public TransactionDetail SystemTransactionDetail;

    public TransactionDatabaseDetail() {
        CardTransactionDetail = new TransactionDetail();
        ButtonTransactionDetail = new TransactionDetail();
        DoorSensorTransactionDetail = new TransactionDetail();
        SoftwareTransactionDetail = new TransactionDetail();
        AlarmTransactionDetail = new TransactionDetail();
        SystemTransactionDetail = new TransactionDetail();
    }

    @Override
    public int GetDataLen() {
        return 0x0D * 6;
    }

    @Override
    public void SetBytes(ByteBuf data) {
        TransactionDetail[] dlst = new TransactionDetail[]{CardTransactionDetail, ButtonTransactionDetail, DoorSensorTransactionDetail,
            SoftwareTransactionDetail, AlarmTransactionDetail, SystemTransactionDetail};
        for (int i = 0; i < dlst.length; i++) {
            dlst[i].SetBytes(data);
        }
        return;
    }

    @Override
    public ByteBuf GetBytes() {
        return null;
    }

}

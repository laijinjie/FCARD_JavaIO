package Face.Data;

import io.netty.buffer.ByteBuf;

/**
 * 记录数据库的详情
 * @author F
 */
public class TransactionDatabaseDetail extends Door.Access.Door8800.Command.Data.TransactionDatabaseDetail {
    /**
     * 体温记录
     */
    public TransactionDetail BodyTemperatureTransactionDetail;
    /**
     * 记录数据库的详情
     */
    public TransactionDatabaseDetail(){
        CardTransactionDetail = new TransactionDetail();
        DoorSensorTransactionDetail = new TransactionDetail();
        SystemTransactionDetail = new TransactionDetail();
        BodyTemperatureTransactionDetail = new TransactionDetail();
    }

    @Override
    public void SetBytes(ByteBuf data) {
        Door.Access.Door8800.Command.Data.TransactionDetail[] ListTransaction;
        if (data.readableBytes() == 52)
        {
             ListTransaction = new Door.Access.Door8800.Command.Data.TransactionDetail[]
                    { CardTransactionDetail, DoorSensorTransactionDetail, SystemTransactionDetail, BodyTemperatureTransactionDetail };
        }
        else
        {
            ListTransaction = new Door.Access.Door8800.Command.Data.TransactionDetail[]
                    { CardTransactionDetail, DoorSensorTransactionDetail, SystemTransactionDetail };
        }

        for (int i = 0; i < ListTransaction.length; i++)
        {
            ListTransaction[i].SetBytes(data);

            if(ListTransaction[i].ReadIndex> ListTransaction[i].WriteIndex)
            {
                ListTransaction[i].ReadIndex = 0;
            }
        }
    }



}

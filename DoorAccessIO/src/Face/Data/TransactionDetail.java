package Face.Data;

/**
 * @author F
 */
public class TransactionDetail extends Door.Access.Door8800.Command.Data.TransactionDetail {
    @Override
    public long readable() {
        if (WriteIndex < ReadIndex)
        {
            return 0;
        }

        if (ReadIndex == WriteIndex)
        {
            return 0;
        }
        return (WriteIndex - ReadIndex);
    }
}

package Face.Elevator.System.Remote;

public class UnlockRelay extends OpenRelay{
    public UnlockRelay(RemoteRelay_Parameter parameter) {
        super(parameter);
    }

    @Override
    protected int RemoteCommandCode() {
        return 0x24;
    }
    @Override
    protected int RemoteCommandPar()  {
        return 1;
    }
}

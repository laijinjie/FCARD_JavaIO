package Face.Elevator.System.Remote;

public class LockRelay extends OpenRelay{
    public LockRelay(RemoteRelay_Parameter parameter) {
        super(parameter);
    }

    @Override
    protected int RemoteCommandCode() {
        return 0x24;
    }
}

package Face.Elevator.System.Remote;

public class HoldRelay extends OpenRelay{
    public HoldRelay(RemoteRelay_Parameter parameter) {
        super(parameter);
    }

    @Override
    protected int RemoteCommandPar()  {
        return 2;
    }
}

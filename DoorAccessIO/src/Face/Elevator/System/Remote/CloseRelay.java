package Face.Elevator.System.Remote;

public class CloseRelay extends OpenRelay{
    public CloseRelay(RemoteRelay_Parameter parameter) {
        super(parameter);
    }
    @Override
    protected int RemoteCommandPar()  {
        return 1;
    }
}

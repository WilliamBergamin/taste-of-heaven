package sample;

//TODO make static object
public class Machine {

    private String machineToken;
    private String eventKey;

    public Machine(String machineToken, String eventKey){
        this.machineToken = machineToken;
        this.eventKey = eventKey;
    }

    public String getMachineToken() {
        return machineToken;
    }

    public String getEventKey() {
        return eventKey;
    }
}

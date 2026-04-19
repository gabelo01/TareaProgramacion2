package cr.ac.una.sistemafichas.model;

public class Ticket {

    private int number;
    private String status; // "waiting", "called", "attended"
    private Client client;
    private Procedure procedure;
    private boolean priority;
    private String creationDate;
    private String stationName;
    private String branchName;

    private String callTime;

    public Ticket() {
    }

    public Ticket(int number, String status, Client client, Procedure procedure,
            boolean priority, String creationDate, String stationName, String branchName) {
        this.number = number;
        this.status = status;
        this.client = client;
        this.procedure = procedure;
        this.priority = priority;
        this.creationDate = creationDate;
        this.stationName = stationName;
        this.branchName = branchName;
    }

    public int getNumber() {
        return number;
    }

    public String getStatus() {
        return status;
    }

    public Client getClient() {
        return client;
    }

    public Procedure getProcedure() {
        return procedure;
    }

    public boolean getPriority() {
        return priority;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public void setProcedure(Procedure procedure) {
        this.procedure = procedure;
    }

    public void setPriority(boolean priority) {
        this.priority = priority;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }


    public String getCallTime() {
        return callTime;
    }

    public void setCallTime(String callTime) {
        this.callTime = callTime;
    }
}

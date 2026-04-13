package cr.ac.una.sistemafichas.model;

public class Employee {

    private String name;
    private String id;
    private String pin;
    private String branchName;
    private String stationName;

    public Employee() {}

    public Employee(String name, String id, String pin, String branchName, String stationName) {
        this.name = name;
        this.id = id;
        this.pin = pin;
        this.branchName = branchName;
        this.stationName = stationName;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public String getPin() {
        return pin;
    }

    public String getBranchName() {
        return branchName;
    }

    public String getStationName() {
        return stationName;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    @Override
    public String toString() {
        return name + " - " + stationName;
    }
}
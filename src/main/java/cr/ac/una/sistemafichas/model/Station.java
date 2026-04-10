package cr.ac.una.sistemafichas.model;

import java.util.List;

public class Station {

    private int number;
    private String name;
    private boolean preferential;
    private String branchName;
    private List<String> procedureNames;

    public Station() {}

    public Station(int number, String name, boolean preferential, String branchName, List<String> procedureNames) {
        this.number = number;
        this.name = name;
        this.preferential = preferential;
        this.branchName = branchName;
        this.procedureNames = procedureNames;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isPreferential() {
        return preferential;
    }

    public void setPreferential(boolean preferential) {
        this.preferential = preferential;
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public List<String> getProcedureNames() {
        return procedureNames;
    }

    public void setProcedureNames(List<String> procedureNames) {
        this.procedureNames = procedureNames;
    }
}

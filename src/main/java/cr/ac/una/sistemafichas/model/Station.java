package cr.ac.una.sistemafichas.model;

import java.util.List;

public class Station {

    private String name;
    private boolean preferential;
    private String branchName;
    private List<String> procedureNames;
    private boolean active;

    public Station() {
    }

    public Station(String name, boolean preferential, String branchName, List<String> procedureNames, boolean active) {
        this.name = name;
        this.preferential = preferential;
        this.branchName = branchName;
        this.procedureNames = procedureNames;
        this.active = active;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public boolean isPreferential() {
        return preferential;
    }

    public void setPreferential(boolean preferential) {
        this.preferential = preferential;
    }

    public List<String> getProcedureNames() {
        return procedureNames;
    }

    public void setProcedureNames(List<String> procedureNames) {
        this.procedureNames = procedureNames;
    }

    public String toString() {
        if (preferential) {
            return name + " - Preferencial";
        }
        return name + " - Comun";

    }
    
    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}

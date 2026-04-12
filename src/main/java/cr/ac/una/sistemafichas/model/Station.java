package cr.ac.una.sistemafichas.model;

import java.util.List;

public class Station {

    private String name;
    private boolean preferential;
    private String branchName;
    private List<Procedure> procedureNames;

    public Station() {
    }

    public Station(String name, boolean preferential, String branchName, List<Procedure> procedureNames) {
        this.name = name;
        this.preferential = preferential;
        this.branchName = branchName;
        this.procedureNames = procedureNames;
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

    public List<Procedure> getProcedureNames() {
        return procedureNames;
    }

    public void setProcedureNames(List<Procedure> procedureNames) {
        this.procedureNames = procedureNames;
    }

    public String toString() {
        if(preferential){
        return name+" - Preferencial";
        }
        return name+" - Comun";
        

    }
}

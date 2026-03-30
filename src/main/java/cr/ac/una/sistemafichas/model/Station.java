package cr.ac.una.sistemafichas.model;

import java.util.List;

public class Station {
    private int number;
    private String name;
    private boolean preferential;
    private List<String> procedureNames; // nombres de trámites que atiende. usamos List<String> procedureNames en lugar de List<Procedure> para que el JSON sea simple y no haya datos duplicados entre archivos

    public Station() {}

    public Station(int number, String name, boolean preferential, List<String> procedureNames) {
        this.number = number;
        this.name = name;
        this.preferential = preferential;
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

    public List<String> getProcedureNames() {
        return procedureNames; 
    }
    public void setProcedureNames(List<String> procedureNames) {
        this.procedureNames = procedureNames;
    }
}
  


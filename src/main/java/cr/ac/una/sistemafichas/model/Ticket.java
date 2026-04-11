package cr.ac.una.sistemafichas.model;

public class Ticket {
    private int number;
    private String status;                            // "waiting", "called", "attended"
    private Client client;                             // puede ser null si no se identificó
    private Procedure procedure;
    private boolean priority;
    private String creationDate; //Anddy: propare lo de date en String, con LocalDate me esta dando fallos
    private int stationNumber;  // para indicadores y proyección
    private String branchName;  // para indicadores por sucursal
    
    public Ticket(){}
  
    public Ticket(int number, String status, Client client, Procedure procedure, boolean priority, String creationDate, int stationNumber, String branchName){
    this.number=number;
    this.status=status;
    this.client=client;
    this.procedure=procedure;
    this.priority=priority;
    this.creationDate=creationDate;
    this.stationNumber=stationNumber;
    this.branchName= branchName;
    
    }
    
    public int getNumber(){
        return number;
    }
    public String getStatus(){
        return status;
    }
    public Client getClient(){
        return client;
    }
    public Procedure getProcedure(){
        return procedure;
    }
    public boolean getPriority(){
        return priority;
    }
    public String getCreationDate(){
        return creationDate;
    }
    public void setNumber(int number){
        this.number=number;
    }
    public void setStatus(String status){
        this.status=status;
    }
    public void setClient(Client client){
        this.client=client;
    }
    public void setProcedure(Procedure procedure){
        this.procedure=procedure;
    }
    public void setPriority(boolean priority){
        this.priority=priority;
    }
    public void setCreationDate(String creationDate){
        this.creationDate=creationDate;
    }

    public int getStationNumber() {
        return stationNumber;
    }

    public void setStationNumber(int stationNumber) {
        this.stationNumber = stationNumber;
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }
    
    
}

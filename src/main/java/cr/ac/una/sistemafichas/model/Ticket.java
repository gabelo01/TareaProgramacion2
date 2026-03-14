package cr.ac.una.sistemafichas.model;

import java.time.LocalDateTime;

public class Ticket {
     private int number;
    private String status;
    private Client client;
    private Procedure procedure;
    private boolean priority;
    private LocalDateTime creationDate;
    
    public Ticket(){}
  
    public Ticket(int number, String status, Client client, Procedure procedure, boolean priority, LocalDateTime creationDate){
    this.number=number;
    this.status=status;
    this.client=client;
    this.procedure=procedure;
    this.priority=priority;
    this.creationDate=creationDate;
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
    public LocalDateTime getCreationDate(){
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
    public void setCreationDate(LocalDateTime creationDate){
        this.creationDate=creationDate;
    }
    
}

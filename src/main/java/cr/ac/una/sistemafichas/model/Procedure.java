package cr.ac.una.sistemafichas.model;


public class Procedure {
    private String name;
    private boolean active;
     
    public Procedure() {
    }
    
    public Procedure(String name, boolean active){
        this.name=name;
        this.active=active;
    }
    public void setName(String name){
        this.name=name;
    }
    public void setActive(boolean active){
        this.active=active;
    }
    public String getName(){
        return name;
    }
    public boolean isActive(){
        return active;
    }
    
}

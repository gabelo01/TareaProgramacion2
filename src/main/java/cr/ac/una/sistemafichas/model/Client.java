package cr.ac.una.sistemafichas.model;

public class Client {
    private String name;
    private String ID;
    private int age;
    private String phoneNumber;
    private String photo;
    
    public Client(String name,String ID, int age, String photo, String phoneNumber){
        this.name=name;
        this.ID=ID;
        this.age=age;
        this.phoneNumber=phoneNumber;
        this.photo=photo;
    }
    public void setName(String name){
        this.name=name;
    }
    public void setID(String ID){
        this.ID=ID;
    }
    public void setAge(int age){
        this.age=age;
    }
    public void setPhoto(String photo){
        this.photo=photo;
    }
    public String getName(){
        return name;        
    }
    public String getID(){
        return ID;        
    }
    public int getAge(){
        return age;        
    }
    public String phoneNumber(){
        return phoneNumber;
    }
    public void setPhoneNumber(String phoneNumber){
        this.phoneNumber=phoneNumber;
    }
    public String getPhoto(){
        return photo;        
    }
}


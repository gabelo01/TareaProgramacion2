package cr.ac.una.sistemafichas.model;

public class Employee extends Person {

    private String password;


    public Employee(String name, String ID, int age, String password) {    // Constructor sin foto para que sea opcional
        super(name, ID, age);
        this.password = password;
    }


    public Employee(String name, String ID, int age, String password, String photo) {
        super(name, ID, age, photo);
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
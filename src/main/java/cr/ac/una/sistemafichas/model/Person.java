package cr.ac.una.sistemafichas.model;

public class Person {
    protected String name;
    protected String ID;
    protected int age;
    protected String photo;

    public Person(String name, String ID, int age) { // Constructor sin foto para que la de empleado sea opcional
        this.name = name;
        this.ID = ID;
        this.age = age;
        this.photo = null;
    }

    // Constructor con foto
    public Person(String name, String ID, int age, String photo) {
        this.name = name;
        this.ID = ID;
        this.age = age;
        this.photo = photo;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getName() {
        return name;
    }

    public String getID() {
        return ID;
    }

    public int getAge() {
        return age;
    }

    public String getPhoto() {
        return photo;
    }
    
    public String toString() {
       return name + " - " + ID + " - " + age;
    }
}
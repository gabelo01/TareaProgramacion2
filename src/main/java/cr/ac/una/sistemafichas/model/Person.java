package cr.ac.una.sistemafichas.model;

import java.time.LocalDate;
import java.time.Period;

public class Person {
    protected String name;
    protected String id;
    private String age;
    protected String photoPath;

    public Person(String name, String id, String age) {     // Constructor sin foto para que sea opcional la de empleado
        this.name = name;
        this.id = id;
        this.age = age;
        this.photoPath = null;
    }

    public Person(String name, String id, String age, String photo) {
        this.name = name;
        this.id = id;
        this.age = age;
        this.photoPath = photo;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public void setPhoto(String photo) {
        this.photoPath = photo;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public String getAge() {
        return age;
    }

    public String getPhoto() {
        return photoPath;
    }

    @Override
    public String toString() {
        return name + " - " + id + " - " + age;
    }
    
    public boolean isValidBirthDate() {
        try {
            LocalDate.parse(this.age);
            return true;
        } catch (Exception e) {
            return false;
        }
    
    }
    
    public int calculateAge() {
        try {
            LocalDate birth = LocalDate.parse(this.age);
            return Period.between(birth, LocalDate.now()).getYears();
        } catch (Exception e) {
            return 0;
        }
    }
    
    
}
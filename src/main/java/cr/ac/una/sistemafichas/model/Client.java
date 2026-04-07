package cr.ac.una.sistemafichas.model;

public class Client {

    private String name;
    private String id;
    private int age;
    private String photo;

    public Client() {
    }
    
    public Client(String name, String id, int age, String photo) {
        this.name = name;
        this.id = id;
        this.age = age;
        this.photo = photo;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setID(String id) {
        this.id = id;
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

    public String getId() {
        return id;
    }

    public int getAge() {
        return age;
    }

    public String getPhoto() {
        return photo;
    }

    @Override
    public String toString() {
        return name + " - " + id + " - " + age;
    }
}

package cr.ac.una.sistemafichas.model;

public class Client extends Person {

    private boolean preferential;

    public Client(String name, String id, String age, String photo, boolean preferential) {
        super(name, id, age, photo);
        this.preferential = preferential;
    }

    public boolean isPreferential() {
        return preferential;
    }

    public void setPreferential(boolean preferential) {
        this.preferential = preferential;
    }
}

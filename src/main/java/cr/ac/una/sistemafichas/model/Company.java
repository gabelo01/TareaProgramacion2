package cr.ac.una.sistemafichas.model;

public class Company {

    private String nombre;
    private String logo; //quemado de momento, luego vemos como agregar un formato png en parametros
    private String pinAdmin;

    public Company() {
    }

    public Company(String nombre, String logo, String pinAdmin) {
        this.nombre = nombre;
        this.logo = logo;
        this.pinAdmin = pinAdmin;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getPinAdmin() {
        return pinAdmin;
    }

    public void setPinAdmin(String pinAdmin) {
        this.pinAdmin = pinAdmin;
    }
}
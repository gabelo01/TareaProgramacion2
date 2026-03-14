package cr.ac.una.sistemafichas.model;
import java.util.List;

public class Branch {

    private String nombre;
    private String direccion;

    public Branch() {
    }

    public Branch(String nombre, String direccion) {
        this.nombre = nombre;
        this.direccion = direccion;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }
}
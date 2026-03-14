package cr.ac.una.sistemafichas.model;

import java.util.List;


public class Station {

    private String nombre;
    private boolean preferencial;

    public Station() {
    }

    public Station(String nombre, boolean preferencial) {
        this.nombre = nombre;
        this.preferencial = preferencial;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public boolean isPreferencial() {
        return preferencial;
    }

    public void setPreferencial(boolean preferencial) {
        this.preferencial = preferencial;
    }
}

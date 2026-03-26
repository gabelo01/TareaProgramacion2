package cr.ac.una.sistemafichas.model;
import java.util.List;
public class Branch {
 private String name;
 private String direction;
 private List<Station> stations;
 
 
 public Branch() {}
   
 public Branch(String name, String direction, List<Station>stations){
     this.name=name;
     this.direction=direction;
     this.stations=stations;
     
 }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public List<Station> getStations() {
        return stations;
    }

    public void setStations(List<Station> stations) {
        this.stations = stations;
    }

 
 
}

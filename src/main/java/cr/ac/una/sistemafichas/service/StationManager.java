/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cr.ac.una.sistemafichas.service;

import cr.ac.una.sistemafichas.model.Station;
import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author diazv
 */
public class StationManager {
    private List<Station> stations;
    
    public StationManager(){
        stations= new ArrayList<>();
    }
    
    public void addStation(Station station){
        stations.add(station);
    }
    public List<Station> getStations(){
        return stations;
    }
    
    public void assignStation(){
        
    }
    public void removeStation(Station station){
        stations.remove(station);
    }
    
}

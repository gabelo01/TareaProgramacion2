/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cr.ac.una.sistemafichas.model;
import java.util.List;
public class Branch {
 private String name;
 private String direction;
 private List<Station> stations;
 
 public Branch(String name, String direction, List<Station>stations){
     this.name=name;
     this.direction=direction;
     this.stations=stations;
     
 }
 // get y setters
}

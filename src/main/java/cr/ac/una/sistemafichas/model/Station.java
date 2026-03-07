/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cr.ac.una.sistemafichas.model;

import java.util.List;
public class Station {
  private int number;
  private boolean priority;
  private List<Procedure> procedures;
  
  public Station(int number, boolean priority, List<Procedure>procedures){
      this.number=number;
      this.priority=priority;
      this.procedures=procedures;
  }
//get y setters
}

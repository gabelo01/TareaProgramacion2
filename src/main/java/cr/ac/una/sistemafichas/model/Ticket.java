/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cr.ac.una.sistemafichas.model;

import java.time.LocalDateTime;

public class Ticket {
     private String number;
    private String status;
    private Client client;
    private Procedure procedure;
    private boolean priority;
    private LocalDateTime creationDate;
  
    public Ticket(String number, String status, Client client, Procedure procedure, boolean priority, LocalDateTime creationDate){
    this.number=number;
    this.status=status;
    this.client=client;
    this.procedure=procedure;
    this.priority=priority;
    this.creationDate=creationDate;
    }
    //get y set
}

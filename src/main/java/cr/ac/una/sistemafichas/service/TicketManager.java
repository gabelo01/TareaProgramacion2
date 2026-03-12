/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cr.ac.una.sistemafichas.service;

import cr.ac.una.sistemafichas.model.Ticket;
import cr.ac.una.sistemafichas.model.Branch;
import java.util.ArrayList;
import java.util.List;

public class TicketManager {
private List<Ticket> tickets;
private int currentNumber;
private int nextNumber;
private Branch branch;

public TicketManager(){
    tickets= new ArrayList<>();
    currentNumber=0;
    nextNumber =1;
}
 
public Ticket generateTicket(){
    Ticket ticket = new Ticket();
    ticket.setNumber(nextNumber);
    tickets.add(ticket);
    nextNumber++;
    return ticket;
    
}
public Ticket callNextTicket(){
    if(currentNumber < tickets.size()){
    Ticket ticket=tickets.get(currentNumber);
    currentNumber++;
    return ticket;
    }
    return null;
}
public Ticket getCurrentTiket(){
    if(currentNumber>0 && currentNumber <= tickets.size()){
        return tickets.get(currentNumber - 1);
    }
    return null;
}
public int getWaitingCount(){
    
    return tickets.size() - currentNumber;   
}
public void resetTickets(){
    currentNumber=0;
    nextNumber=1;
    tickets.clear();
}
}

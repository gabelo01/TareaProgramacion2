package cr.ac.una.sistemafichas.service;

import cr.ac.una.sistemafichas.model.Ticket;
import cr.ac.una.sistemafichas.util.EmployeeSessionManager;
import cr.ac.una.sistemafichas.util.JsonUtil;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.Arrays;
import java.util.List;

public class TicketService {

    private static TicketService instance;

    private ObservableList<Ticket> tickets = FXCollections.observableArrayList();

    private final String PATH = "data/tickets.json";

    private int nextNumber = 1;

    private java.util.List<Runnable> listeners = new java.util.ArrayList<>();

    private TicketService() {
        load();
    }

    public static TicketService getInstance() {
        if (instance == null) {
            instance = new TicketService();
        }
        return instance;
    }

    public ObservableList<Ticket> getTickets() {
        return tickets;
    }

    public void addListener(Runnable listener) {
        listeners.add(listener);

    }

    private void notifyListeners() {
        for (Runnable l : listeners) {
            l.run();
        }
    }

    public void load() {
        try {
            Ticket[] t = JsonUtil.read(PATH, Ticket[].class);
            if (t != null) {
                tickets.setAll(Arrays.asList(t));
                nextNumber = tickets.stream()
                        .mapToInt(Ticket::getNumber)
                        .max()
                        .orElse(0) + 1;
            }
        } catch (Exception e) {
            System.out.println("Error loading tickets");
        }
    }

    public void save() {
        JsonUtil.write(PATH, tickets);
        notifyListeners();
    }

    public Ticket generateTicket(Ticket ticket) {

        load();

        ticket.setNumber(nextNumber++);
        ticket.setStatus("waiting");

        tickets.add(ticket);

        save();

        return ticket;
    }

    public Ticket callNext() {
        Ticket t = tickets.stream()
                .filter(x -> x.getStatus().equals("waiting"))
                .filter(x -> x.getBranchName().equals(EmployeeSessionManager.getBranchName()))
                .findFirst()
                .orElse(null);

        if (t != null) {
            t.setStatus("called");
            save();
        }

        return t;
    }

    public long getWaitingCount() {
        return tickets.stream()
                .filter(t -> t.getStatus().equals("waiting"))
                .count();
    }

    public Ticket getNextWaiting() {
        return tickets.stream().filter(t -> t.getStatus().equals("waiting")).findFirst().orElse(null);
    }

    public Ticket getLastCalled() {
        List<Ticket> tickets = getTickets();

        for (int i = tickets.size() - 1; i >= 0; i--) {
            if ("called".equals(tickets.get(i).getStatus())) {
                return tickets.get(i);
            }
        }

        return null;
    }

    public void notifyAll_() {
        notifyListeners();
    }

    public Ticket getLatestTicket() {
        load();

        return tickets.stream()
                .filter(t -> t.getNumber() > 0)
                .max((a, b) -> Integer.compare(a.getNumber(), b.getNumber()))
                .orElse(null);
    }
}

package cr.ac.una.sistemafichas.service;

import cr.ac.una.sistemafichas.model.Ticket;
import cr.ac.una.sistemafichas.util.AppContext;
import cr.ac.una.sistemafichas.util.JsonUtil;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.util.Arrays;
import java.util.List;

public class TicketService {

    private static TicketService instance;

    private ObservableList<Ticket> tickets = FXCollections.observableArrayList();

    private final String PATH = "tickets.json";

    private int nextNumber = 1;

    private java.util.List<Runnable> listeners = new java.util.ArrayList<>();

    private TicketService() {
        load();
    }

    private String getBranch() {
        return (String) AppContext.getInstance().get("branch");
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
            Ticket[] array = JsonUtil.read(PATH, Ticket[].class);

            if (array != null) {
                tickets.setAll(Arrays.asList(array));

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
        try {
            Ticket[] existing = JsonUtil.read(PATH, Ticket[].class);

            List<Ticket> all = existing != null
                    ? new java.util.ArrayList<>(Arrays.asList(existing))
                    : new java.util.ArrayList<>();

            String branch = getBranch();

            all.removeIf(t -> branch != null
                    && branch.equalsIgnoreCase(t.getBranchName()));

            all.addAll(tickets);

            JsonUtil.write(PATH, all);

            notifyListeners();

        } catch (Exception e) {
            System.out.println("Error saving tickets");
        }
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
        String branch = getBranch();

        Ticket t = tickets.stream()
                .filter(x -> "waiting".equals(x.getStatus()))
                .filter(x -> branch != null
                && x.getBranchName() != null
                && branch.equalsIgnoreCase(x.getBranchName()))
                .findFirst()
                .orElse(null);

        if (t != null) {
            t.setStatus("called");
            save();
        }

        return t;
    }

    public long getWaitingCount() {
        String branch = getBranch();

        return tickets.stream()
                .filter(t -> "waiting".equals(t.getStatus()))
                .filter(t -> branch != null
                && t.getBranchName() != null
                && branch.equalsIgnoreCase(t.getBranchName()))
                .count();
    }

    public Ticket getNextWaiting() {
        String branch = getBranch();

        return tickets.stream()
                .filter(t -> "waiting".equals(t.getStatus()))
                .filter(t -> branch != null
                && t.getBranchName() != null
                && branch.equalsIgnoreCase(t.getBranchName()))
                .findFirst()
                .orElse(null);
    }

    public Ticket getLastCalled() {
        String branch = getBranch();

        List<Ticket> list = getTickets();

        for (int i = list.size() - 1; i >= 0; i--) {
            Ticket t = list.get(i);

            if ("called".equals(t.getStatus())
                    && branch != null
                    && t.getBranchName() != null
                    && branch.equalsIgnoreCase(t.getBranchName())) {
                return t;
            }
        }

        return null;
    }

    public void notifyAll_() {
        notifyListeners();
    }

    public Ticket getLatestTicket() {
        load();

        String branch = getBranch();

        return tickets.stream()
                .filter(t -> t.getNumber() > 0)
                .filter(t -> branch != null
                && t.getBranchName() != null
                && branch.equalsIgnoreCase(t.getBranchName()))
                .max((a, b) -> Integer.compare(a.getNumber(), b.getNumber()))
                .orElse(null);
    }
}

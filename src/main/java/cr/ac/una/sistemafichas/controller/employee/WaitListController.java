package cr.ac.una.sistemafichas.controller.employee;

import cr.ac.una.sistemafichas.controller.Controller;
import cr.ac.una.sistemafichas.model.Ticket;
import cr.ac.una.sistemafichas.service.TicketService;
import cr.ac.una.sistemafichas.util.FlowController;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class WaitListController extends Controller {

    @FXML private TableView<Ticket> tblTickets;
    @FXML private TableColumn<Ticket, Integer> colNumber;
    @FXML private TableColumn<Ticket, String> colProcedure;
    @FXML private TableColumn<Ticket, String> colType;
    @FXML private TableColumn<Ticket, String> colID;
    @FXML private TableColumn<Ticket, String> colName;
    @FXML private MFXTextField txtSearchName;
    @FXML private MFXTextField txtSearchID;
    @FXML private CheckBox chkPreferential;
    @FXML private MFXTextField txtTicketNumber;

    private FilteredList<Ticket> filteredTickets;
    @FXML
    private MFXButton btnCallSelected;
    @FXML
    private MFXButton btnCallByNumber;

    @Override
    public void initialize() {
        setupColumns();
        loadTickets();
        setupFilters();
    }

    private void setupColumns() {
        colNumber.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getNumber()).asObject());
        colProcedure.setCellValueFactory(data ->new SimpleStringProperty(data.getValue().getProcedure() != null? data.getValue().getProcedure().getName() : " "));
        colType.setCellValueFactory(data ->new SimpleStringProperty(data.getValue().getPriority() ? "Preferencial" : "Normal"));
        colID.setCellValueFactory(data ->new SimpleStringProperty(data.getValue().getClient() != null? data.getValue().getClient().getId(): "Sin Registrar"));
        colName.setCellValueFactory(data ->new SimpleStringProperty(data.getValue().getClient() != null? data.getValue().getClient().getName(): "Sin Registrar"
    )
);
    }

    private void loadTickets() {
        TicketService.getInstance().load();
        ObservableList<Ticket> all = TicketService.getInstance().getTickets();
        filteredTickets = new FilteredList<>(all, t -> "waiting".equals(t.getStatus()));
        SortedList<Ticket> sorted = new SortedList<>(filteredTickets);
        sorted.comparatorProperty().bind(tblTickets.comparatorProperty());
        tblTickets.setItems(sorted);
    }

    private void setupFilters() {
        txtSearchName.textProperty().addListener((obs, o, n) -> applyFilters());
        txtSearchID.textProperty().addListener((obs, o, n) -> applyFilters());
        chkPreferential.selectedProperty().addListener((obs, o, n) -> applyFilters());
    }

    private void applyFilters() {
        filteredTickets.setPredicate(ticket -> {
            if (ticket == null) return false;
            if (!"waiting".equals(ticket.getStatus())) return false;

            String name = txtSearchName.getText();
            String id   = txtSearchID.getText();

            boolean matchName = name == null || name.isEmpty() ||
                (ticket.getClient() != null &&
                 ticket.getClient().getName().toLowerCase().contains(name.toLowerCase()));

            boolean matchID = id == null || id.isEmpty() ||
                (ticket.getClient() != null &&
                 ticket.getClient().getId().toLowerCase().contains(id.toLowerCase()));

            boolean matchPref = !chkPreferential.isSelected() || ticket.getPriority();

            return matchName && matchID && matchPref;
        });
    }

    @FXML
    private void onActionBtnCallSelected() {
        Ticket selected = tblTickets.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Selecciona el campo del Tickete en la Lista.");
            return;
        }
        callTicket(selected);
    }

    @FXML
    private void onActionBtnCallByNumber() {
        try {
            int num = Integer.parseInt(txtTicketNumber.getText().trim());
            Ticket t = TicketService.getInstance().getTickets().stream()
                .filter(x -> x.getNumber() == num && "waiting".equals(x.getStatus()))
                .findFirst().orElse(null);
            if (t != null) {
                callTicket(t);
            } else {
                showAlert("Ticket " + num + " no encontrado o ya no esta en espera");
            }
        } catch (NumberFormatException e) {
            showAlert("Ingresa un numero de ticket valido.");
        }
    }

    private void callTicket(Ticket t) {
        t.setStatus("called");
        TicketService.getInstance().save();
        TicketService.getInstance().setLastCalled(t);
        FlowController.getInstance().goViewReplace("employee/StationView");
    }

    @FXML
    private void onActionBtnBack() {
        FlowController.getInstance().goViewReplace("employee/StationView");
    }

    protected void showAlert(String msg) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setHeaderText(null);
        a.setContentText(msg);
        a.showAndWait();
    }
}
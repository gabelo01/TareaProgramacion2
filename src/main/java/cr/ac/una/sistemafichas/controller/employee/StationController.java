package cr.ac.una.sistemafichas.controller.employee;

import com.google.gson.reflect.TypeToken;
import cr.ac.una.sistemafichas.controller.Controller;
import cr.ac.una.sistemafichas.model.Branch;
import cr.ac.una.sistemafichas.model.CompanyConfig;
import cr.ac.una.sistemafichas.model.Station;
import cr.ac.una.sistemafichas.model.Ticket;
import cr.ac.una.sistemafichas.service.TicketService;
import cr.ac.una.sistemafichas.util.EmployeeSessionManager;
import cr.ac.una.sistemafichas.util.FlowController;
import cr.ac.una.sistemafichas.util.JsonUtil;
import cr.ac.una.sistemafichas.util.Mensaje;
import java.io.File;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

public class StationController extends Controller {

    @FXML
    private ImageView imgLogo;
    @FXML
    private Label lblCompanyName;
    @FXML
    private Label lblStationName;
    @FXML
    private Label lblTime;
    @FXML
    private Label lblCurrentNumber;
    @FXML
    private Label lblWaitingCount;

    private static final String CONFIG_PATH = "data/config.json";

    private Station station;
    private Timeline clockTimeline;
    private Timeline refreshTimeline;

    private final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

    @Override
    public void initialize() {
        loadHeader();
        loadStation();

        startClock();
        startAutoRefresh();

        clearCurrentTicket();
        updateWaitingCount();

        Ticket last = getLastCalledByStation();
        if (last != null) {
            showCurrentTicket(last);
        }

        listen();
    }

    private void loadHeader() {
        CompanyConfig config = JsonUtil.read(CONFIG_PATH, CompanyConfig.class);
        if (config == null) {
            return;
        }

        lblCompanyName.setText(config.getCompanyName());

        try {
            File file = new File(config.getLogoPath());
            if (file.exists()) {
                imgLogo.setImage(new Image(file.toURI().toString()));
            }
        } catch (Exception e) {
            System.out.println("Error loading logo");
        }
    }

    private void loadStation() {
        String stationName = EmployeeSessionManager.getStationName();
        String branchName = EmployeeSessionManager.getBranchName();

        if (stationName == null || branchName == null) {
            lblStationName.setText("Sin estación asignada");
            return;
        }

        Type type = new TypeToken<List<Branch>>() {
        }.getType();
        List<Branch> branches = JsonUtil.read("data/branches.json", type);

        if (branches == null) {
            return;
        }

        station = branches.stream()
                .filter(b -> b.getName().equalsIgnoreCase(branchName))
                .flatMap(b -> b.getStations().stream())
                .filter(s -> s.getName().equalsIgnoreCase(stationName))
                .findFirst()
                .orElse(null);

        refreshStationLabel();
    }

    private void reloadStationState() { // revisa estado real de estación desde JSON
        loadStation(); // refresca config sin depender de memoria vieja
    }

    private void refreshStationLabel() {
        if (station != null) {
            lblStationName.setText(
                    station.getName() + " | Sucursal: " + station.getBranchName()
            );
        } else {
            lblStationName.setText("Estación no encontrada");
        }
    }

    private void startClock() {
        if (clockTimeline != null) {
            clockTimeline.stop();
        }

        clockTimeline = new Timeline(new KeyFrame(Duration.seconds(1), e
                -> lblTime.setText(LocalDateTime.now().format(timeFormatter))
        ));

        clockTimeline.setCycleCount(Timeline.INDEFINITE);
        clockTimeline.play();
    }

    private void startAutoRefresh() {
        if (refreshTimeline != null) {
            refreshTimeline.stop();
        }

        refreshTimeline = new Timeline(new KeyFrame(Duration.seconds(5), e -> {
            TicketService.getInstance().load();

            updateWaitingCount();

            reloadStationState();// se actualiza estado como tickets y activa pero no todo
        }));

        refreshTimeline.setCycleCount(Timeline.INDEFINITE);
        refreshTimeline.play();
    }

    private void updateWaitingCount() {
        List<Ticket> tickets = TicketService.getInstance().getTickets();

        if (tickets == null) {
            lblWaitingCount.setText("0");
            return;
        }

        long count = tickets.stream()
                .filter(t -> "waiting".equals(t.getStatus()))
                .count();

        lblWaitingCount.setText(String.valueOf(count));
    }

    public void showCurrentTicket(Ticket t) {
        if (t == null) {
            return;
        }
        lblCurrentNumber.setText(String.valueOf(t.getNumber()));
    }

    private void clearCurrentTicket() {
        lblCurrentNumber.setText("0");
    }

    private boolean isStationValid() {
        if (station == null) {
            showAlert("Estación no configurada.");
            return false;
        }

        if (!station.isActive()) {
            showAlert("La estación está inactiva.");
            return false;
        }

        if (station.getProcedureNames() == null || station.getProcedureNames().isEmpty()) {
            showAlert("La estación no tiene trámites configurados.");
            return false;
        }

        return true;
    }

    @FXML
    private void onActionBtnNext() {

        reloadStationState();

        if (!isStationValid()) {
            return;
        }
        String branch = EmployeeSessionManager.getBranchName();
        TicketService service = TicketService.getInstance();

        Ticket last = getLastCalledByStation();// limpiar último ticket de esta estación
        if (last != null) {
            last.setStatus("attended");
        }

        Ticket t = service.getTickets().stream()
                .filter(ticket -> "waiting".equals(ticket.getStatus()))
                .filter(ticket -> ticket.getBranchName() != null)
                .filter(ticket -> ticket.getBranchName().equalsIgnoreCase(branch))
                .filter(ticket -> ticket.getProcedure() != null)
                .filter(ticket -> ticket.getProcedure() != null && station.getProcedureNames().stream().anyMatch(p -> p.trim().equalsIgnoreCase(ticket.getProcedure().getName().trim())))
                .sorted((a, b) -> Integer.compare(a.getNumber(), b.getNumber()))
                .findFirst()
                .orElse(null);

        if (t != null) {
            t.setStatus("called");
            t.setStationName(station.getName());
            t.setCallTime(LocalDateTime.now().toString());

            service.save();
            service.notifyAll_();

            showCurrentTicket(t);
            updateWaitingCount();
        } else {
            showAlert("No hay tickets para esta estación.");
        }
    }

    @FXML
    private void onActionBtnPreferential() {

        reloadStationState();

        if (!isStationValid()) {
            return;
        }

        if (!station.isPreferential()) {
            showAlert("Esta estación no atiende preferenciales.");
            return;
        }

        TicketService service = TicketService.getInstance();

        Ticket last = getLastCalledByStation();  // limpiar último
        if (last != null) {
            last.setStatus("attended");
        }

        Ticket t = service.getTickets().stream()
                .filter(x -> "waiting".equals(x.getStatus()))
                .filter(x -> x.getPriority())
                .filter(x -> x.getProcedure() != null)
                .filter(x -> station.getProcedureNames()
                .contains(x.getProcedure().getName()))
                .findFirst()
                .orElse(null);

        if (t != null) {
            t.setStatus("called");
            t.setStationName(station.getName());
            t.setCallTime(LocalDateTime.now().toString());

            service.save();
            service.notifyAll_();

            showCurrentTicket(t);
            updateWaitingCount();
        } else {
            showAlert("No hay tickets preferenciales.");
        }
    }

    @FXML
    private void onActionBtnRepeat() {

        Ticket last = getLastCalledByStation();

        if (last != null) {
            showCurrentTicket(last);
        } else {
            showAlert("No hay ticket llamado en esta estación.");
        }
    }

    @FXML
    private void onActionBtnExit(ActionEvent event) {
        boolean confirmar = new Mensaje()
                .showConfirmation("Salir", getStage(), "¿Desea cerrar la ventana?");

        if (confirmar) {
            FlowController.getInstance().salir();
        }
    }

    protected void showAlert(String msg) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setHeaderText(null);
        a.setContentText(msg);
        a.showAndWait();
    }

    @FXML
    private void onActionBtnListCurrentClient() {
        FlowController.getInstance().limpiarLoader("employee/WaitListView");
        FlowController.getInstance().goViewInWindow("employee/WaitListView");
    }

    @FXML
    private void onActionBtnIndicators(ActionEvent event) {
        FlowController.getInstance().goViewInWindow("employee/IndicatorsView");
    }

    @FXML
    private void onActionBtnClient(ActionEvent event) {
        FlowController.getInstance().goViewInWindow("employee/CurrentClientView");
    }

    @FXML
    private void onActionBtnRegisterClient(ActionEvent event) {
        FlowController.getInstance().goViewInWindow("admin/MaintenanceClientView");
    }

    private void listen() {

        TicketService.getInstance().addListener(() -> {
            javafx.application.Platform.runLater(() -> {

                Ticket last = getLastCalledByStation();

                if (last != null) {
                    showCurrentTicket(last);
                }

                updateWaitingCount();
            });
        });
    }

    private Ticket getLastCalledByStation() {

        if (station == null) {
            return null;
        }

        return TicketService.getInstance().getTickets().stream()
                .filter(t -> "called".equals(t.getStatus()))
                .filter(t -> station.getName().equals(t.getStationName()))
                .filter(t -> t.getCallTime() != null)
                .sorted((a, b) -> LocalDateTime.parse(b.getCallTime())
                .compareTo(LocalDateTime.parse(a.getCallTime())))
                .findFirst()
                .orElse(null);
    }
}

package cr.ac.una.sistemafichas.controller.employee;

import cr.ac.una.sistemafichas.controller.Controller;
import cr.ac.una.sistemafichas.model.CompanyConfig;
import cr.ac.una.sistemafichas.model.Station;
import cr.ac.una.sistemafichas.model.Ticket;
import cr.ac.una.sistemafichas.service.TicketService;
import cr.ac.una.sistemafichas.util.FlowController;
import cr.ac.una.sistemafichas.util.JsonUtil;
import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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

    @FXML private ImageView imgLogo;
    @FXML private Label lblCompanyName;
    @FXML private Label lblStationName;
    @FXML private Label lblTime;
    @FXML private Label lblCurrentNumber;
    @FXML private Label lblWaitingCount;

    private static final String CONFIG_PATH  = "data/config.json";
    private static final String STATION_PATH = "data/configStation.json";

    private Station station;
    private Timeline clockTimeline;
    private final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
    private Timeline refreshTimeline;

    @Override
    public void initialize() {
        loadHeader();
        loadStation();
        startClock();
        startAutoRefresh(); // para actualizar tickets y leer json
        clearCurrentTicket();
        updateWaitingCount();

        Ticket last = TicketService.getInstance().getLastCalled();
        if (last != null) showCurrentTicket(last);
    }

    private void loadHeader() {
        CompanyConfig config = JsonUtil.read(CONFIG_PATH, CompanyConfig.class);
        if (config == null) return;

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

//    private void loadStation() {
//        station = JsonUtil.read(STATION_PATH, Station.class);
//        if (station != null) {
//            lblStationName.setText("Estación " + station.getNumber() +
//                                  " - " + station.getName());
//        }
//    }
    
    private void loadStation() {
    station = JsonUtil.read(STATION_PATH, Station.class);

    if (station == null) {
        lblStationName.setText("Estación no configurada");
        return;
    }
        lblStationName.setText(station.getNumber() +" | Nombre: " + station.getName() +" | Sucursal: " + station.getBranchName());
    }

    private void startClock() {
        if (clockTimeline != null) clockTimeline.stop();

        clockTimeline = new Timeline(new KeyFrame(Duration.seconds(1), e ->
            lblTime.setText(LocalDateTime.now().format(timeFormatter))
        ));

        clockTimeline.setCycleCount(Timeline.INDEFINITE);
        clockTimeline.play();
    }
    
    private void startAutoRefresh() {
        if (refreshTimeline != null) refreshTimeline.stop();

        refreshTimeline = new Timeline(new KeyFrame(Duration.seconds(5), e -> {
            TicketService.getInstance().load();
            updateWaitingCount();
        }));
        refreshTimeline.setCycleCount(Timeline.INDEFINITE);
        refreshTimeline.play();
    }

    private void updateWaitingCount() {
        long count = TicketService.getInstance().getWaitingCount();
        lblWaitingCount.setText("" +count);
    }

    public void showCurrentTicket(Ticket t) {
        if (t == null) return;

        lblCurrentNumber.setText(String.valueOf(t.getNumber()));
    }

    private void clearCurrentTicket() {
        lblCurrentNumber.setText("0");
    }
    
    @FXML
    private void onActionBtnNext() {

        if (station == null || station.getProcedureNames() == null) {
            showAlert("Estación no configurada.");
            return;
        }

        TicketService service = TicketService.getInstance();
        Ticket t = null;

        if (station.isPreferential()) { // Si la estación es preferencial, buscamos primero preferenciales
            t = service.getTickets().stream()
                    .filter(ticket -> "waiting".equals(ticket.getStatus()))
                    .filter(ticket -> ticket.getProcedure() != null)
                    .filter(ticket -> ticket.getPriority()) // solo atendera alos  preferenciales con este filtro
                    .filter(ticket -> station.getProcedureNames().contains(ticket.getProcedure().getName()))
                    .findFirst()
                    .orElse(null);
        }

        if (t == null) { // Si no encontra preferencial o no es preferencial, buscamos un ticket compatible
            t = service.getTickets().stream()
                    .filter(ticket -> "waiting".equals(ticket.getStatus()))
                    .filter(ticket -> ticket.getProcedure() != null)
                    .filter(ticket -> station.getProcedureNames().contains(ticket.getProcedure().getName()))
                    .findFirst().orElse(null);
        }

    
        if (t != null) {
            t.setStatus("called"); // Si se encontra se llamamos y se actualiza
            service.save();
            service.setLastCalled(t);showCurrentTicket(t);updateWaitingCount();
        } else {
            showAlert("No hay tickets para los trámites de esta estación.");
        }
    }

    @FXML
    private void onActionBtnRepeat() {
        Ticket last = TicketService.getInstance().getLastCalled();
        if (last != null) {
            showCurrentTicket(last);
        } else {
            showAlert("No hay ticket llamado.");
        }
    }

    @FXML
    private void onActionBtnListCurrentClient() {
        FlowController.getInstance().goViewReplace("employee/WaitListView");
    }

    private void onActionBtnIndicators() {
        FlowController.getInstance().goViewReplace("employee/IndicatorsView");
    }

    @FXML
    private void onActionBtnClient() {
        FlowController.getInstance().goViewReplace("employee/CurrentClientView");
    }
    
    @FXML
    private void onActionBtnPreferential() {
        
        if (station == null || station.getProcedureNames() == null) {
            showAlert("Estación no configurada.");
            return;
        }
        
        TicketService service = TicketService.getInstance();

        Ticket t = service.getTickets().stream().filter(x -> "waiting".equals(x.getStatus())).filter(x -> x.getPriority()).filter(x -> x.getProcedure() != null)
                      .filter(x -> station.getProcedureNames().contains(x.getProcedure().getName())).findFirst().orElse(null);

        if (t != null) {
            t.setStatus("called");
            service.save();
            service.setLastCalled(t);
            showCurrentTicket(t);
            updateWaitingCount();
         } else {
            showAlert("No hay tickets preferenciales en espera para esta estacion.");
        }
    }
    
    protected void showAlert(String msg) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setHeaderText(null);
        a.setContentText(msg);
        a.showAndWait();
    }

    @FXML
    private void onActionBtnRegisterClient(ActionEvent event) {
        FlowController.getInstance().goViewReplace("admin/MaintenanceClientView");
    }

    @FXML
    private void onActionBtnExit(ActionEvent event) {
        FlowController.getInstance().goViewReplace("employee/StationLogin");
    }
    
}
package cr.ac.una.sistemafichas.controller.kiosk;

import cr.ac.una.sistemafichas.controller.Controller;
import cr.ac.una.sistemafichas.model.Client;
import cr.ac.una.sistemafichas.model.CompanyConfig;
import cr.ac.una.sistemafichas.model.Procedure;
import cr.ac.una.sistemafichas.model.Station;
import cr.ac.una.sistemafichas.model.Ticket;
import cr.ac.una.sistemafichas.service.TicketService;
import cr.ac.una.sistemafichas.util.FlowController;
import cr.ac.una.sistemafichas.util.JsonUtil;
import cr.ac.una.sistemafichas.util.KioskSessionManager;
import cr.ac.una.sistemafichas.util.PdfUtil;
import com.google.gson.reflect.TypeToken;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXListView;

import java.io.File;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class SelectProceduresController extends Controller {

    @FXML private MFXButton btnGetTicket;
    @FXML private MFXButton btnPreferential;
    @FXML private Label lblName;
    @FXML private Label lblClientInfo;
    @FXML private ImageView imgLogo;
    @FXML private MFXListView<String> listProcedures;

    private static final String CONFIG_PATH  = "data/config.json";
    private static final String STATION_PATH = "data/configStation.json";
    private static final String PROC_PATH    = "data/procedures.json";

    private static boolean preferentialOverride = false;

    public static void setPreferentialOverride(boolean value) {
        preferentialOverride = value;
    }

    @Override
    public void initialize() {
        loadHeader();
        loadProcedures();
        loadClientInfo();
    }

    private void loadHeader() {
        CompanyConfig config = JsonUtil.read(CONFIG_PATH, CompanyConfig.class);
        if (config == null) return;

        if (lblName != null) lblName.setText(config.getCompanyName());

        try {
            File file = new File(config.getLogoPath());
            if (file.exists() && imgLogo != null) {
                imgLogo.setImage(new Image(file.toURI().toString()));
            }
        } catch (Exception e) {
            System.out.println("Error cargando logo");
        }
    }

    private void loadProcedures() {
        if (listProcedures == null) return;

        Station station = JsonUtil.read(STATION_PATH, Station.class);
        if (station == null) return;

        Type type = new TypeToken<List<Procedure>>(){}.getType();
        List<Procedure> allProcedures = JsonUtil.read(PROC_PATH, type);
        if (allProcedures == null) return;

        List<String> filtered = allProcedures.stream()
                .filter(Procedure::isActive)
                .filter(p -> station.getProcedureNames().contains(p.getName()))
                .map(Procedure::getName)
                .toList();

        listProcedures.setItems(FXCollections.observableArrayList(filtered));
    }

    private void loadClientInfo() {
        Client client = KioskSessionManager.getCurrentClient();

        if (lblClientInfo == null) return;

        if (client != null) {
            lblClientInfo.setText("Bienvenido: " + client.getName());
            
            if (client.isPreferential() || isClientPreferential(client)) {
                btnPreferential.setVisible(false); // para ocultar boton de ir a preferencial si ya lo es
            }
        } else {
            lblClientInfo.setText("Invitado");
        }
    }

    @FXML
    private void OnActionBtnGetTicket(ActionEvent event) {

        String selected = null;

        if (listProcedures != null &&!listProcedures.getSelectionModel().getSelectedValues().isEmpty()) {
            selected = listProcedures.getSelectionModel().getSelectedValues().get(0);
        }

        if (selected == null) {
            showAlert("Seleccione un trámite.");
            return;
        }

        Client client = KioskSessionManager.getCurrentClient();

 
        boolean isPriority = false;

        if (client != null) {
            if (client.isPreferential()) { // si cliente esta marcado como preferencial cambia la prioridad
                isPriority = true;
            }
            else if (isClientPreferential(client)) {  // o si adulto con 65 a'os o mas
                isPriority = true;
            }
        }


        if (preferentialOverride) { //preferencial manual con PreferentialView
         isPriority = true;
        }
        
        

        Procedure procedure = new Procedure(selected, true);

        Ticket ticket = new Ticket();
        ticket.setProcedure(procedure);
        ticket.setClient(client);
        ticket.setPriority(isPriority);
        ticket.setCreationDate(LocalDateTime.now().toString());
        ticket.setStatus("waiting");

        Type type = new TypeToken<List<Station>>(){}.getType();
        Station stations = JsonUtil.read(STATION_PATH, type);
        if (stations != null) {
            Station station = KioskSessionManager.getStation();
            ticket.setStationName(station.getName());
        }

        TicketService.getInstance().generateTicket(ticket);
        CompanyConfig config = JsonUtil.read(CONFIG_PATH, CompanyConfig.class);
        PdfUtil.generateTicketPdf(ticket, config);

        showAlert("Ticket #" + ticket.getNumber() + " generado.");
        preferentialOverride = false; //despues de generar ticket y mandar info se resetea el preferencial

        KioskSessionManager.clearSession();
        FlowController.getInstance().goViewReplace("kiosk/LoginView");
    }

    @FXML
    private void OnActionBtnPreferential(ActionEvent event) {
        FlowController.getInstance().goViewReplace("kiosk/Preferential");
    }

    private boolean isClientPreferential(Client client) {
        if (client == null) return false;

        try {
            LocalDate birth = LocalDate.parse(client.getAge());
            int age = Period.between(birth, LocalDate.now()).getYears();
            return age >= 65;
        } catch (Exception e) {
            return false;
        }
    }

    private void showAlert(String msg) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setHeaderText(null);
        a.setContentText(msg);
        a.show(); //muestra exito de crear ticket
    }

    @FXML
    private void OnActionBtnCancel(ActionEvent event) {  //Para limpiar el login y la info del ticket si cancelo
        preferentialOverride = false;
        KioskSessionManager.clearSession();

        FlowController.getInstance().goViewReplace("kiosk/LoginView");
    }
}

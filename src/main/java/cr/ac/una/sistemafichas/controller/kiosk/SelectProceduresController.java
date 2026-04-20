package cr.ac.una.sistemafichas.controller.kiosk;

import com.google.gson.reflect.TypeToken;
import cr.ac.una.sistemafichas.controller.Controller;
import cr.ac.una.sistemafichas.model.Branch;
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
import io.github.palexdev.materialfx.controls.MFXButton;

import java.io.File;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.List;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;

public class SelectProceduresController extends Controller {

    @FXML
    private Label lblName;
    @FXML
    private Label lblClientInfo;
    @FXML
    private ImageView imgLogo;
    @FXML
    private VBox vboxProcedures;

    private static final String CONFIG_PATH = "data/config.json";
    private static final String PROC_PATH = "data/procedures.json";

    private static boolean preferentialOverride = false;

    private javafx.animation.Timeline refreshTimeline;

    private String selectedProcedure = null;

    public static void setPreferentialOverride(boolean value) {
        preferentialOverride = value;
    }
    @FXML
    private ImageView imgPreferential;
    @FXML
    private MFXButton btnGetTicket;

    @Override
    public void initialize() {
        loadHeader();
        loadProcedures();
        loadClientInfo();

        startAutoRefresh();
    }

    private void loadHeader() {
        CompanyConfig config = JsonUtil.read(CONFIG_PATH, CompanyConfig.class);

        if (config == null) {
            return;
        }

        lblName.setText(config.getCompanyName());
        try {
            File file = new File(config.getLogoPath());

            if (file.exists()) {
                imgLogo.setImage(new Image(file.toURI().toString()));
            }
            if (imgPreferential != null) {
                File filePreferential = new File("data/images/Preferential.png");
                if (filePreferential.exists()) {
                    imgPreferential.setImage(new Image(filePreferential.toURI().toString()));
                }
            }
        } catch (Exception e) {
            System.out.println("Error cargando logo");
        }
    }

    private void startAutoRefresh() {

        if (refreshTimeline != null) {
            refreshTimeline.stop();
        }

        refreshTimeline = new javafx.animation.Timeline(
                new javafx.animation.KeyFrame(javafx.util.Duration.seconds(5), e -> {
                    loadProcedures();
                })
        );

        refreshTimeline.setCycleCount(javafx.animation.Timeline.INDEFINITE);
        refreshTimeline.play();
    }

    private void loadProcedures() {
        if (vboxProcedures == null) {
            return;
        }

        vboxProcedures.getChildren().clear();

        String branchName = KioskSessionManager.getBranch();

        if (branchName == null) {
            branchName = "Buenos Aires";
            KioskSessionManager.setBranch(branchName);
        }

        Type branchType = new TypeToken<List<Branch>>() {
        }.getType();
        List<Branch> branches = JsonUtil.read("data/branches.json", branchType);
        if (branches == null) {
            return;
        }

        final String finalBranchName = branchName;
        Branch current = branches.stream()
                .filter(b -> b.getName().equalsIgnoreCase(finalBranchName))
                .findFirst()
                .orElse(null);

        if (current == null || current.getStations() == null) {
            return;
        }

        Type procType = new TypeToken<List<Procedure>>() {
        }.getType();
        List<Procedure> allProcedures = JsonUtil.read(PROC_PATH, procType);
        if (allProcedures == null) {
            return;
        }

        List<String> availableProcedures = current.getStations().stream().filter(st -> st.isActive()) //solo tramites de estaciones activas
                .flatMap(st -> st.getProcedureNames().stream()).distinct().toList();

        List<String> filtered = allProcedures.stream()
                .filter(Procedure::isActive)
                .map(Procedure::getName)
                .filter(availableProcedures::contains)
                .toList();

        for (String procName : filtered) {
            MFXButton btn = new MFXButton(procName);
            btn.setMaxWidth(Double.MAX_VALUE);
            btn.setPrefHeight(50);
            btn.setStyle(
                    "-fx-font-size: 16px;"
                    + "-fx-background-radius: 10;"
                    + "-fx-padding: 10;"
            );
            btn.setOnAction(e -> selectedProcedure = procName);
            vboxProcedures.getChildren().add(btn);
        }

        if (selectedProcedure != null && !filtered.contains(selectedProcedure)) {
            selectedProcedure = null; // limpiar por si selección es inválida, que admin quite tramite 
        }
    }

    private void loadClientInfo() {
        Client client = KioskSessionManager.getCurrentClient();
        if (client != null) {
            lblClientInfo.setText("Bienvenido: " + client.getName());

            boolean isPreferencial = client.isPreferential() || isClientPreferential(client);

            if (imgPreferential != null) {
                imgPreferential.setVisible(!isPreferencial); // muestra boton si no es preferencial
            }

        } else {
            lblClientInfo.setText("Invitado");

            if (imgPreferential != null) {
                imgPreferential.setVisible(true); // invitado puede usar preferencial siempre
            }
        }
    }

    @FXML
    private void OnActionBtnGetTicket(ActionEvent event) {

        if (selectedProcedure == null) {
            showAlert("Seleccione un trámite.");
            return;
        }

        Client client = KioskSessionManager.getCurrentClient();
        boolean isPriority = false;

        if (client != null && (client.isPreferential() || isClientPreferential(client))) {
            isPriority = true;
        }
        if (preferentialOverride) {
            isPriority = true;
        }

        String branchName = KioskSessionManager.getBranch();
        String assignedStation = resolveStation(branchName, selectedProcedure);

        Procedure procedure = new Procedure(selectedProcedure, true);

        Ticket ticket = new Ticket();
        ticket.setBranchName(branchName);
        ticket.setStationName(assignedStation);
        ticket.setProcedure(procedure);
        ticket.setClient(client);
        ticket.setPriority(isPriority);
        ticket.setCreationDate(LocalDateTime.now().toString());
        ticket.setStatus("waiting");

        TicketService.getInstance().generateTicket(ticket);

        TicketService.getInstance().setLastTicket(ticket);

        CompanyConfig config = JsonUtil.read(CONFIG_PATH, CompanyConfig.class);
        PdfUtil.generateTicketPdf(ticket, config);

        Notifications.create()
                .title("PDF")
                .text("Ticket #" + ticket.getNumber() + " generado.")
                .position(Pos.BOTTOM_RIGHT)
                .hideAfter(Duration.seconds(2))
                .showInformation();

        preferentialOverride = false;
        KioskSessionManager.clearClient();

        FlowController.getInstance().goView("kiosk/TicketConfirmationView");
    }

    @FXML
    private void OnActionBtnCancel(ActionEvent event) {
        preferentialOverride = false;
        KioskSessionManager.clearClient();
        FlowController.getInstance().goView("kiosk/LoginKioskView");
    }

    private boolean isClientPreferential(Client client) {

        if (client == null) {
            return false;
        }

        try {
            LocalDate birth = LocalDate.parse(client.getAge());
            return Period.between(birth, LocalDate.now()).getYears() >= 65;
        } catch (Exception e) {
            return false;
        }
    }

    private String resolveStation(String branchName, String procedureName) {
        if (branchName == null || procedureName == null) {
            return null;
        }
        Type branchType = new TypeToken<List<Branch>>() {
        }.getType();
        List<Branch> branches = JsonUtil.read("data/branches.json", branchType);
        if (branches == null) {
            return null;
        }
        return branches.stream()
                .filter(b -> b.getName().equalsIgnoreCase(branchName))
                .findFirst()
                .map(Branch::getStations)
                .filter(stations -> stations != null)
                .flatMap(stations -> stations.stream()
                .filter(Station::isActive)
                .filter(st -> st.getProcedureNames() != null
                && st.getProcedureNames().contains(procedureName))
                .map(Station::getName)
                .findFirst())
                .orElse(null);
    }

    private void showAlert(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.show();
    }

    @FXML
    private void OnClickedImage(MouseEvent event) {
        FlowController.getInstance().goView("kiosk/Preferential");
    }
}

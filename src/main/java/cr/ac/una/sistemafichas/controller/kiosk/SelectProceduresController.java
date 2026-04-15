package cr.ac.una.sistemafichas.controller.kiosk;

import com.google.gson.reflect.TypeToken;
import cr.ac.una.sistemafichas.controller.Controller;
import cr.ac.una.sistemafichas.model.Branch;
import cr.ac.una.sistemafichas.model.Client;
import cr.ac.una.sistemafichas.model.CompanyConfig;
import cr.ac.una.sistemafichas.model.Procedure;
import cr.ac.una.sistemafichas.model.Ticket;
import cr.ac.una.sistemafichas.service.TicketService;
import cr.ac.una.sistemafichas.util.FlowController;
import cr.ac.una.sistemafichas.util.JsonUtil;
import cr.ac.una.sistemafichas.util.KioskSessionManager;
import cr.ac.una.sistemafichas.util.PdfUtil;
import io.github.palexdev.materialfx.controls.MFXButton;

import java.io.File;
import java.lang.reflect.Type;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.List;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

public class SelectProceduresController extends Controller {

    @FXML private MFXButton btnGetTicket;
    @FXML private MFXButton btnPreferential;
    @FXML private Label lblName;
    @FXML private Label lblClientInfo;
    @FXML private ImageView imgLogo;
    @FXML private VBox vboxProcedures;

    private static final String CONFIG_PATH = "data/config.json";
    private static final String PROC_PATH = "data/procedures.json";

    private static boolean preferentialOverride = false;

    private String selectedProcedure = null;

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

        lblName.setText(config.getCompanyName());

        try {
            File file = new File(config.getLogoPath());
            if (file.exists()) {
                imgLogo.setImage(new Image(file.toURI().toString()));
            }
        } catch (Exception e) {
            System.out.println("Error cargando logo");
        }
    }

    private void loadProcedures() {
        if (vboxProcedures == null){
            return;
        }

        vboxProcedures.getChildren().clear();

        String branchName = KioskSessionManager.getBranch();


        if (branchName == null) {
            branchName = "Buenos Aires";
            KioskSessionManager.setBranch(branchName);
        }

        Type branchType = new TypeToken<List<Branch>>(){}.getType();
        List<Branch> branches = JsonUtil.read("data/branches.json", branchType);
        if (branches == null){
            return;
        }

        final String finalBranchName = branchName;
        Branch current = branches.stream()
                .filter(b -> b.getName().equalsIgnoreCase(finalBranchName))
                .findFirst()
                .orElse(null);

        if (current == null || current.getStations() == null) return;

        Type procType = new TypeToken<List<Procedure>>(){}.getType();
        List<Procedure> allProcedures = JsonUtil.read(PROC_PATH, procType);
        if (allProcedures == null) return;

        List<String> availableProcedures = current.getStations().stream()
                .flatMap(st -> st.getProcedureNames().stream())
                .distinct()
                .toList();

        List<String> filtered = allProcedures.stream()
                .filter(Procedure::isActive)
                .map(Procedure::getName)
                .filter(availableProcedures::contains)
                .toList();

        //crear botones de los tramites
        for (String procName : filtered) {
            MFXButton btn = new MFXButton(procName);

            btn.setMaxWidth(Double.MAX_VALUE);
            btn.setPrefHeight(50);

            btn.setStyle(
                    "-fx-font-size: 16px;" +
                    "-fx-background-radius: 10;" +
                    "-fx-padding: 10;"
            );

            btn.setOnAction(e -> seleccionarProcedimiento(procName));

            vboxProcedures.getChildren().add(btn);
        }
    }

    private void seleccionarProcedimiento(String procName) {
        selectedProcedure = procName;
    }

    private void loadClientInfo() {
        Client client = KioskSessionManager.getCurrentClient();

        if (client != null) {
            lblClientInfo.setText("Bienvenido: " + client.getName());

            if (client.isPreferential() || isClientPreferential(client)) {
                btnPreferential.setVisible(false);
            }
        } else {
            lblClientInfo.setText("Invitado");
        }
    }

    @FXML
    private void OnActionBtnGetTicket(ActionEvent event) {

        String selected = selectedProcedure;

        if (selected == null) {
            showAlert("Seleccione un trámite.");
            return;
        }

        Client client = KioskSessionManager.getCurrentClient();

        boolean isPriority = false;

        if (client != null) {
            if (client.isPreferential() || isClientPreferential(client)) {
                isPriority = true;
            }
        }

        if (preferentialOverride) {
            isPriority = true;
        }

        Procedure procedure = new Procedure(selected, true);

        Ticket ticket = new Ticket();
        ticket.setBranchName(KioskSessionManager.getBranch());
        ticket.setProcedure(procedure);
        ticket.setClient(client);
        ticket.setPriority(isPriority);
        ticket.setCreationDate(LocalDateTime.now().toString());
        ticket.setStatus("waiting");

        TicketService.getInstance().generateTicket(ticket);

        CompanyConfig config = JsonUtil.read(CONFIG_PATH, CompanyConfig.class);
        PdfUtil.generateTicketPdf(ticket, config);

        showAlert("Ticket #" + ticket.getNumber() + " generado.");

        preferentialOverride = false;

        KioskSessionManager.clearClient();

        FlowController.getInstance().goView("kiosk/LoginKioskView");
    }

    @FXML
    private void OnActionBtnPreferential(ActionEvent event) {
        FlowController.getInstance().goView("kiosk/Preferential");
    }

    @FXML
    private void OnActionBtnCancel(ActionEvent event) {
        preferentialOverride = false;
        KioskSessionManager.clearClient();
        FlowController.getInstance().goView("kiosk/LoginKioskView");
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
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.show();
    }
}
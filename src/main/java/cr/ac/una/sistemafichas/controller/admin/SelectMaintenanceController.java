package cr.ac.una.sistemafichas.controller.admin;

import cr.ac.una.sistemafichas.controller.Controller;
import cr.ac.una.sistemafichas.model.CompanyConfig;
import cr.ac.una.sistemafichas.util.FlowController;
import cr.ac.una.sistemafichas.util.JsonUtil;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.io.File;
import javafx.event.ActionEvent;

public class SelectMaintenanceController extends Controller {

    @FXML private Label lbCompany;
    @FXML private ImageView imgLogo;
    private static final String CONFIG_PATH = "data/config.json";

    @Override
    public void initialize() {
        cargarEncabezado();
    }

    // Cierra la sesión y regresa al login de admin
    @FXML
    private void onActionBtnVolver() {
        FlowController.getInstance().goViewReplace("admin/LoginAdminView");
    }

    // Abre la vista de mantenimiento de Trámites
    @FXML
    private void onActionBtnProcedures(ActionEvent event) {
        FlowController.getInstance().goView("admin/ProceduresMaintenanceView");
    }

    // Abre la vista de mantenimiento de Sucursales
    @FXML
    private void onActionBtnBranches(ActionEvent event) {
        FlowController.getInstance().goView("admin/BranchMaintenanceView");
    }

    @FXML
    private void onActionBtnIndicators(ActionEvent event) {
        FlowController.getInstance().goView("Indicators");
    }

    @FXML
    private void onActionBtnClients(ActionEvent event) {
        FlowController.getInstance().goView("admin/MaintenanceClientView");
    }

    @FXML
    private void onActionBtnConfig(ActionEvent event) {
        FlowController.getInstance().goView("admin/ConfigView");
    }

    private void cargarEncabezado() {
        CompanyConfig config = JsonUtil.read(CONFIG_PATH, CompanyConfig.class);
        if (config == null) return;

        if (lbCompany != null) {
            lbCompany.setText(config.getCompanyName());
        }

        try {
            File logoFile = new File(config.getLogoPath());
            if (logoFile.exists() && imgLogo != null) {
                imgLogo.setImage(new Image(logoFile.toURI().toString()));
            }
        } catch (Exception e) {
            System.err.println("No se pudo cargar el logo.");
        }
    }
}

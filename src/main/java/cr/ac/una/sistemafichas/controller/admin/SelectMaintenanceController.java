package cr.ac.una.sistemafichas.controller.admin;

import cr.ac.una.sistemafichas.controller.Controller;
import cr.ac.una.sistemafichas.model.CompanyConfig;
import cr.ac.una.sistemafichas.util.FlowController;
import cr.ac.una.sistemafichas.util.JsonUtil;
import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.io.File;
import javafx.event.ActionEvent;

public class SelectMaintenanceController extends Controller {

    @FXML private MFXButton btnIndicators;
    @FXML private MFXButton btnClients;
    @FXML private MFXButton btnConfig;
    @FXML private MFXButton btnVolver;
    @FXML private Label lbCompany;
    @FXML private ImageView imgLogo;

    private static final String CONFIG_PATH = "data/config.json";
    
    @FXML
    private MFXButton btnBranchesAndProcedures;
    
    @Override
    public void initialize() {
        cargarEncabezado();
    }
    
    @FXML
    private void btnVolver() {
        FlowController.getInstance().goViewReplace("admin/LoginAdminView");
    }

    @FXML
    private void btnBranchesAndProcedures(ActionEvent event) {
        FlowController.getInstance().goViewReplace("admin/ProceduresAndBranchMaintenanceView");
    }
    
    @FXML
    private void btnIndicators() {
        FlowController.getInstance().goViewReplace("Indicators");
    }

    @FXML
    private void btnClients() {
        FlowController.getInstance().goViewReplace("Clients");
    }

    @FXML
    private void btnConfig() {
        FlowController.getInstance().goViewReplace("admin/ConfigView");
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
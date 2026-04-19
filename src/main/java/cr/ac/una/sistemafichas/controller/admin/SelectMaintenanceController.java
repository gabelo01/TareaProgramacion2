package cr.ac.una.sistemafichas.controller.admin;

import cr.ac.una.sistemafichas.controller.Controller;
import cr.ac.una.sistemafichas.model.CompanyConfig;
import cr.ac.una.sistemafichas.util.FlowController;
import cr.ac.una.sistemafichas.util.JsonUtil;
import cr.ac.una.sistemafichas.util.Mensaje;
import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.io.File;
import javafx.event.ActionEvent;
import javafx.stage.Stage;

public class SelectMaintenanceController extends Controller {

    @FXML private Label lbCompany;
    @FXML private ImageView imgLogo;
    private static final String CONFIG_PATH = "data/config.json";
    @FXML
    private MFXButton btnClean;

    @Override
    public void initialize() {
        loadHeader();
    }

    @FXML
    private void onActionBtnProcedures(ActionEvent event) {
        FlowController.getInstance().goView("admin/ProceduresMaintenanceView");
        
    }

    @FXML
    private void onActionBtnBranches(ActionEvent event) {
        FlowController.getInstance().goView("admin/BranchMaintenanceView");
    }

    @FXML
    private void onActionBtnIndicators(ActionEvent event) {
        FlowController.getInstance().goView("admin/Indicators");
    }

    @FXML
    private void onActionBtnClients(ActionEvent event) {
        FlowController.getInstance().goView("admin/MaintenanceClientView");
    }

    @FXML
    private void onActionBtnConfig(ActionEvent event) {
        FlowController.getInstance().goView("admin/ConfigView");
    }

    private void loadHeader() {
        CompanyConfig config = JsonUtil.read(CONFIG_PATH, CompanyConfig.class);
        if (config == null) return;

        if (lbCompany != null){
            lbCompany.setText(config.getCompanyName());
        }

        try {
            File file = new File(config.getLogoPath());
            if (file.exists() && imgLogo != null) {
                imgLogo.setImage(new Image(file.toURI().toString()));
            }
        } catch (Exception e) {
            System.out.println("Error cargando logo");
        }
    }

    @FXML
    private void onActionBtnClean(ActionEvent event) {
        Stage stage = (Stage) lbCompany.getScene().getWindow();
        FlowController.getInstance().goViewInStage("admin/SelectMaintenance",stage);
        
    }

    @FXML
    private void onActionBtnExit(ActionEvent event) {

        boolean confirmar = new Mensaje().showConfirmation("Salir",getStage(),"¿Está seguro que desea salir?");

        if (confirmar) {
            FlowController.getInstance().salir();
        }
    }

    @FXML
    private void onActionBtnEmployees(ActionEvent event) {
        FlowController.getInstance().goView("admin/MaintenanceEmployeeView");
    }

    @FXML
    private void onActionBtnProceduresForStation(ActionEvent event) {
        FlowController.getInstance().goView("admin/MaintenanceStationView");
    }

    
}

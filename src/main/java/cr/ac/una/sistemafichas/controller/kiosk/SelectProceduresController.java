package cr.ac.una.sistemafichas.controller.kiosk;

import cr.ac.una.sistemafichas.controller.Controller;
import cr.ac.una.sistemafichas.model.CompanyConfig;
import cr.ac.una.sistemafichas.util.FlowController;
import cr.ac.una.sistemafichas.util.JsonUtil;
import io.github.palexdev.materialfx.controls.MFXButton;
import java.io.File;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class SelectProceduresController extends Controller {

    @FXML private MFXButton btnGetTicket;
    @FXML private MFXButton btnPreferential;
    @FXML private Label lblName;
    @FXML private ImageView imgLogo;
    private static final String CONFIG_PATH = "data/config.json";

    @Override
    public void initialize() {
        loadHeader(); 
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
    
    @FXML
    private void OnActionBtnGetTicket(ActionEvent event) {
        System.out.println("Obtendria ticket PDF");
    }

    @FXML
    private void OnActionBtnPreferential(ActionEvent event) {
        FlowController.getInstance().goViewReplace("kiosk/Preferential");
    }
}
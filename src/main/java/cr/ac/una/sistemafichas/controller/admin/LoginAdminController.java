package cr.ac.una.sistemafichas.controller.admin;

import cr.ac.una.sistemafichas.controller.Controller;
import cr.ac.una.sistemafichas.model.CompanyConfig;
import cr.ac.una.sistemafichas.util.FlowController;
import cr.ac.una.sistemafichas.util.JsonUtil;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXPasswordField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

public class LoginAdminController extends Controller {

    @FXML private MFXPasswordField pswPin;
    @FXML private MFXButton btnIngresar;

    private static final String CONFIG_PATH = "data/config.json";

    @FXML
    private MFXButton btnSalir;

    @Override
    public void initialize() {
        // los botones ya tienen onAction en el FXML
        // solo limpiamos el campo al entrar
        pswPin.clear();
    }

    @FXML
    private void btnIngresar() {
        CompanyConfig config = JsonUtil.read(CONFIG_PATH, CompanyConfig.class);

        if (config == null) {
            System.err.println("No se pudo leer config.json");
            return;
        }

        if (pswPin.getText().equals(config.getAdminPin())) {
            pswPin.clear();
            Stage stage = (Stage) btnIngresar.getScene().getWindow();
            FlowController.getInstance().goViewInStage("admin/SelectMaintenance",stage);
        } else {
            pswPin.clear();
            pswPin.setFloatingText("PIN incorrecto, intente de nuevo");
        }
    }

    @FXML
    private void btnSalir(ActionEvent event) {
        pswPin.clear();
        FlowController.getInstance().salir();
    }

    @FXML
        private void onKeyPressedIngresar(KeyEvent event) {
            if (event.getCode() == KeyCode.ENTER) {
            btnIngresar();
        }
    }   
 
}
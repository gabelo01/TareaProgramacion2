package cr.ac.una.sistemafichas.controller;

import cr.ac.una.sistemafichas.model.CompanyConfig;
import cr.ac.una.sistemafichas.util.FlowController;
import cr.ac.una.sistemafichas.util.JsonUtil;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXPasswordField;
import javafx.fxml.FXML;

public class LogingAdminController extends Controller {

    @FXML private MFXPasswordField pswPin;
    @FXML private MFXButton btnIngresar;
    @FXML private MFXButton btnVolver;

    private static final String CONFIG_PATH = "data/config.json";

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
            FlowController.getInstance().goView("SelectMaintenance");
        } else {
            pswPin.clear();
            pswPin.setFloatingText("PIN incorrecto, intente de nuevo");
        }
    }

    @FXML
    private void btnVolver() {
        pswPin.clear();
        FlowController.getInstance().goView("Main");
    }
}
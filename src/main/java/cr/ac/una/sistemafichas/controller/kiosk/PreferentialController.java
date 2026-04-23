package cr.ac.una.sistemafichas.controller.kiosk;

import cr.ac.una.sistemafichas.controller.Controller;
import cr.ac.una.sistemafichas.model.CompanyConfig;
import cr.ac.una.sistemafichas.util.FlowController;
import cr.ac.una.sistemafichas.util.Formato;
import cr.ac.una.sistemafichas.util.JsonUtil;
import io.github.palexdev.materialfx.controls.MFXPasswordField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class PreferentialController extends Controller {

    @FXML
    private MFXPasswordField pswPin;
    @FXML
    private Label lblError;

    private static final String CONFIG_PATH = "config.json";

    @Override
    public void initialize() {
        pswPin.delegateSetTextFormatter(Formato.getInstance().integerFormat());
        if (pswPin != null) {
            pswPin.clear();
        }
        if (lblError != null) {
            lblError.setVisible(false);
            lblError.setText("");
        }
    }

    @FXML
    private void OnActionBtnGetFichaPreferential(ActionEvent event) {
        validatePin();
    }

    @FXML
    private void OnActionBtnExit(ActionEvent event) {
        FlowController.getInstance().goView("kiosk/SelectProcedures");
    }

    private void validatePin() {
        CompanyConfig config = JsonUtil.read(CONFIG_PATH, CompanyConfig.class);

        if (config == null) {
            showError("Error leyendo configuración.");
            return;
        }

        String pin = pswPin.getText();
        if (pin == null || pin.isBlank()) {
            showError("Ingrese el PIN.");
            return;
        }

        if (pin != null && pin.equals(config.getAdminPin())) {
            SelectProceduresController.setPreferentialOverride(true); //activa linea de Bolean preferential o priority y luego vuelve a select procedures view
            FlowController.getInstance().goView("kiosk/SelectProcedures");

        } else {
            pswPin.clear();
            showError("PIN incorrecto.");
        }
    }

    private void showError(String msg) {
        if (lblError != null) {
            lblError.setText(msg);
            lblError.setVisible(true);
        }
    }

}

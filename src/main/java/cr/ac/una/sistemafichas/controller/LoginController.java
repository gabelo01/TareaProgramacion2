package cr.ac.una.sistemafichas.controller;

import cr.ac.una.sistemafichas.util.FlowController;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;

public class LoginController extends Controller {

    @FXML private PasswordField password;
    @FXML private Button ingresar;
    @FXML private Label lblError;

    @Override
    public void initialize() {
        if (lblError != null) lblError.setVisible(false);
        ingresar.setOnAction(e -> validarID());
    }

    private void validarID() {
        String pin = password.getText();
        if (pin.equals("123")) {
            password.clear();
            FlowController.getInstance().goView("SelectProcedures");
        } else {
            if (lblError != null) {
                lblError.setText("PIN incorrecto");
                lblError.setVisible(true);
            }
        }
    }
}
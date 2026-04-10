package cr.ac.una.sistemafichas.controller.kiosk;

import cr.ac.una.sistemafichas.controller.Controller;
import cr.ac.una.sistemafichas.util.FlowController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

public class LoginController extends Controller {

    @FXML private PasswordField password;
    private Button ingresar;
    private Label lblError;
    @FXML
    private AnchorPane root;
    @FXML
    private Label lblSaludation;
    @FXML
    private ImageView imgUser;
    @FXML
    private ImageView imgPassword;

    @Override
    public void initialize() {
        if (lblError != null) lblError.setVisible(false);
        ingresar.setOnAction(e -> validarID());
    }

    private void validarID() {
        String pin = password.getText();
        if (pin.equals("123")) {
            password.clear();
            FlowController.getInstance().goView("kiosk/SelectProcedures");
        } else {
            if (lblError != null) {
                lblError.setText("PIN incorrecto");
                lblError.setVisible(true);
            }
        }
    }

    @FXML
    private void OnActionBtnGuest(ActionEvent event) {
    }

    @FXML
    private void OnActionBtn(ActionEvent event) {
    }
}
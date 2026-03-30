package cr.ac.una.sistemafichas.controller.admin;

import cr.ac.una.sistemafichas.util.FlowController;
import io.github.palexdev.materialfx.controls.MFXButton;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;


public class ConfigController implements Initializable {

    @FXML
    private TextField txtCompanyName;
    @FXML
    private PasswordField txtPin;
    @FXML
    private TextField txtLogo;
    @FXML
    private Button btnSelectLogo;
    @FXML
    private Label lblMensaje;
    @FXML
    private MFXButton btnSave;
    @FXML
    private MFXButton btnReturn;

  
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void btnSave(ActionEvent event) {
    }

    @FXML
    private void btnReturn(ActionEvent event) {
        FlowController.getInstance().goViewReplace("admin/SelectMaintenance");
    }

    @FXML
    private void btnSelectLogo(ActionEvent event) {
    }
    
    
}

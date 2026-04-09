package cr.ac.una.sistemafichas.controller.employee;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXPasswordField;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.input.KeyEvent;

public class StationLoginController implements Initializable {

    @FXML
    private MFXPasswordField pswPin;
    @FXML
    private MFXButton btnIngresar;
    @FXML
    private MFXButton btnSalir;

    /**
     * Initializes the controller class.
     */
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void onKeyPressedIngresar(KeyEvent event) {
    }

    @FXML
    private void btnIngresar(ActionEvent event) {
    }

    @FXML
    private void btnSalir(ActionEvent event) {
    }
    
}

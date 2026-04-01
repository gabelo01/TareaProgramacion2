package cr.ac.una.sistemafichas.controller.admin;

import io.github.palexdev.materialfx.controls.MFXTextField;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

public class MaintenanceClientController implements Initializable {

    @FXML
    private AnchorPane root;
    @FXML
    private ListView<?> listClientes;
    @FXML
    private MFXTextField txtNombre;
    @FXML
    private MFXTextField txtId;
    @FXML
    private MFXTextField txtEdad;
    @FXML
    private ImageView imgFoto;

    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }    

    @FXML
    private void OnActionBtnAgregarFoto(ActionEvent event) {
    }

    @FXML
    private void OnActionBtnGuardar(ActionEvent event) {
    }

    @FXML
    private void OnActionBtnEliminar(ActionEvent event) {
    }
    
}

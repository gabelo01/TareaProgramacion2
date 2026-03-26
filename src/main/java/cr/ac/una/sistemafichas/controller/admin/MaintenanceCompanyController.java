package cr.ac.una.sistemafichas.controller.admin;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import java.io.File;
import javafx.scene.image.Image;

public class MaintenanceCompanyController implements Initializable {

    @FXML
    private ImageView imgLogo;
    @FXML
    private Button btnSelectLogo;
    @FXML
    private Button btnSaveCompany;
    private String logoPath;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void OnActionBtnSeleccionarLogo(ActionEvent event) {
    }

    @FXML
    private void OnActionSaveCompany(ActionEvent event) {
    }
    @FXML
    private void SeleccionarLogo(){
        FileChooser chooser = new FileChooser();
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Imagenes","*.png","*.jpg","*.jpeg"));
        chooser.setTitle("Seleccionar Logo");
        File file = chooser.showOpenDialog(null);
        if(file!=null){
            String route = file.getAbsolutePath();
            Image image = new Image(file.toURI().toString());
            imgLogo.setImage(image);
        }
    }
}

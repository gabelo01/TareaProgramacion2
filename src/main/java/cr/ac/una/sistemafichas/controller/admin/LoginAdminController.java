package cr.ac.una.sistemafichas.controller.admin;

import cr.ac.una.sistemafichas.controller.Controller;
import cr.ac.una.sistemafichas.model.CompanyConfig;
import cr.ac.una.sistemafichas.util.FlowController;
import cr.ac.una.sistemafichas.util.Formato;
import cr.ac.una.sistemafichas.util.JsonUtil;
import cr.ac.una.sistemafichas.util.Mensaje;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXPasswordField;
import java.io.File;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class LoginAdminController extends Controller{

    @FXML private MFXPasswordField pswPin;
    @FXML private MFXButton btnIngresar;

    private static final String CONFIG_PATH = "config.json";

    @FXML
    private MFXButton btnSalir;
    @FXML
    private AnchorPane root;
    @FXML
    private Label lblCompanyName;
    @FXML
    private ImageView imgLogo;
    @FXML
    private ImageView imgPassword;

    @Override
    public void initialize() {
        loadHeader();
        pswPin.delegateSetTextFormatter(Formato.getInstance().integerFormat());
        pswPin.clear();
    }
    
    
    private void loadHeader() {
        CompanyConfig config = JsonUtil.read(CONFIG_PATH, CompanyConfig.class);
        if (config == null){
            return;
        }

        if (lblCompanyName != null){
            lblCompanyName.setText(config.getCompanyName());
        }

        try {
            File file = new File(config.getLogoPath());
            if (file.exists() && imgLogo != null) {
                imgLogo.setImage(new Image(file.toURI().toString()));
            }
            
           if (imgPassword != null) {
                File filePassword = new File(JsonUtil.getDataPath() + "images/Password.png");
                if (filePassword.exists()) {
                imgPassword.setImage(new Image(filePassword.toURI().toString()));
                }
            }
        } catch (Exception e) {
            System.out.println("Error cargando logo");
        }
    }
    
    @FXML
    private void  OnActionBtnIngresar() {
        CompanyConfig config = JsonUtil.read(CONFIG_PATH, CompanyConfig.class);

        if (config == null) {
            System.err.println("No se pudo leer config.json");
            return;
        }

        if (pswPin.getText().equals(config.getAdminPin())) {
            pswPin.clear();
            
            FlowController.getInstance().goMain("admin/SelectMaintenance");
            getStage().close();
            
        } else {
            pswPin.clear();
     
            pswPin.setFloatingText("PIN incorrecto, intente de nuevo");
        }
    }
   @FXML
    private void OnActionBtnSalir(ActionEvent event) {

        boolean confirmar = new Mensaje().showConfirmation("Salir",getStage(),"¿Está seguro que desea cerrar esta ventana?");

     if (confirmar) {
            pswPin.clear();
            getStage().close();
        }
    }

    @FXML
        private void onKeyPressedIngresar(KeyEvent event) {
            if (event.getCode() == KeyCode.ENTER) {
            OnActionBtnIngresar();
        }
    }   
}
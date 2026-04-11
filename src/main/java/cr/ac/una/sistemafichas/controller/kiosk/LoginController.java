package cr.ac.una.sistemafichas.controller.kiosk;

import cr.ac.una.sistemafichas.controller.Controller;
import cr.ac.una.sistemafichas.model.Client;
import cr.ac.una.sistemafichas.util.FlowController;
import cr.ac.una.sistemafichas.util.JsonUtil;
import com.google.gson.reflect.TypeToken;
import cr.ac.una.sistemafichas.model.CompanyConfig;
import cr.ac.una.sistemafichas.util.KioskSessionManager;
import io.github.palexdev.materialfx.controls.MFXTextField;
import java.io.File;
import java.lang.reflect.Type;
import java.util.List;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

public class LoginController extends Controller {

    @FXML private MFXTextField txtId;
    @FXML private Label lblError;

    private static final String CLIENTS_PATH = "data/clients.json";
    private static final String CONFIG_PATH = "data/config.json";
    
    @FXML
    private AnchorPane root;
    @FXML
    private Label lblSaludation;
    @FXML
    private ImageView imgLogo;
    @FXML
    private Label lblName;
    @FXML
    private ImageView imgUser;

    @Override
    public void initialize() {
        loadHeader();
        KioskSessionManager.clearSession();
        if (lblError != null) lblError.setVisible(false);
        if (txtId != null) txtId.clear();
    }

    @FXML
    private void OnActionBtn(ActionEvent event) {
        String id = txtId.getText().trim();

        if (id.isEmpty()) {
            showError("Ingrese su cédula.");
            return;
        }

        Type type = new TypeToken<List<Client>>(){}.getType();
        List<Client> clients = JsonUtil.read(CLIENTS_PATH, type);

        if (clients == null || clients.isEmpty()) {
            showError("No hay clientes registrados. Use Invitado.");
            return;
        }

        Client found = clients.stream()
            .filter(c -> c.getId().equals(id))
            .findFirst()
            .orElse(null);

        if (found != null) {
            KioskSessionManager.setCurrentClient(found);
            FlowController.getInstance().goViewReplace("kiosk/SelectProcedures");
        } else {
            showError("Cédula no encontrada. Use el botón Invitado.");
        }
    }
    
        private void loadHeader() {
        CompanyConfig config = JsonUtil.read(CONFIG_PATH, CompanyConfig.class);
        if (config == null){
            return;
        }
        if (lblName != null){
            lblName.setText(config.getCompanyName());
        }
        try {
            File file = new File(config.getLogoPath());
            if (file.exists() && imgLogo != null) {
                imgLogo.setImage(new Image(file.toURI().toString()));
            }
        } catch (Exception e) {
            System.out.println("Error cargandoel logo");
        }
    }

    @FXML
    private void OnActionBtnGuest(ActionEvent event) {
        KioskSessionManager.clearSession();
        FlowController.getInstance().goViewReplace("kiosk/SelectProcedures");
    }

    private void showError(String msg) {
        if (lblError != null) {
            lblError.setText(msg);
            lblError.setVisible(true);
        }
    }
}
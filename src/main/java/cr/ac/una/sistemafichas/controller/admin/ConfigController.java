package cr.ac.una.sistemafichas.controller.admin;

import cr.ac.una.sistemafichas.controller.Controller;
import cr.ac.una.sistemafichas.model.CompanyConfig;
import cr.ac.una.sistemafichas.util.JsonUtil;
import io.github.palexdev.materialfx.controls.MFXButton;
import java.io.File;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;

public class ConfigController extends Controller {

    @FXML private TextField     txtCompanyName;
    @FXML private PasswordField txtPin;
    @FXML private TextField     txtLogo;
    @FXML private MFXButton     btnSelectLogo;
    @FXML private Label         lblMensaje;
    @FXML private MFXButton     btnSave;

    private static final String CONFIG_PATH = "config.json";
    private boolean hasChanges = false;

    @Override
    public void initialize() {
        if (lblMensaje != null) lblMensaje.setVisible(false);
        loadData();

        txtCompanyName.textProperty().addListener((obs, old, nuevo) -> hasChanges = true);
        txtPin.textProperty().addListener((obs, old, nuevo) -> hasChanges = true);
        txtLogo.textProperty().addListener((obs, old, nuevo) -> hasChanges = true);
    }

    private void loadData() {
        CompanyConfig config = JsonUtil.read(CONFIG_PATH, CompanyConfig.class);
        if (config == null) return;
        txtCompanyName.setText(config.getCompanyName());
        txtPin.setText(config.getAdminPin());
        txtLogo.setText(config.getLogoPath());
    }

    @FXML
    private void OnActionBtnSave(ActionEvent event) {
        String nombre = txtCompanyName.getText().trim();
        String pin    = txtPin.getText().trim();
        String logo   = txtLogo.getText().trim();

        if (nombre.isEmpty() || pin.isEmpty()) return;

        CompanyConfig config = new CompanyConfig();
        config.setCompanyName(nombre);
        config.setAdminPin(pin);
        config.setLogoPath(logo);

        JsonUtil.write(CONFIG_PATH, config);
        hasChanges = false;
    }

    @FXML
    private void OnActionBtnSelectLogo(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccionar logo");
        fileChooser.getExtensionFilters().add(
            new FileChooser.ExtensionFilter("Imágenes", "*.png", "*.jpg", "*.jpeg", "*.gif")
        );

        File file = fileChooser.showOpenDialog(getStage());
        if (file != null) {
            try {
                File destDir = new File("JsonUtil.getDataPath() + images");
                destDir.mkdirs();
                File dest = new File(destDir, file.getName());
                java.nio.file.Files.copy(
                    file.toPath(), dest.toPath(),
                    java.nio.file.StandardCopyOption.REPLACE_EXISTING
                );
                txtLogo.setText("images/" + file.getName());
            } catch (Exception e) {
                System.err.println("Error copiando imagen: " + e.getMessage());
            }
        }
    }
}

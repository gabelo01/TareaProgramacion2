package cr.ac.una.sistemafichas.controller.admin;

import com.google.gson.reflect.TypeToken;
import cr.ac.una.sistemafichas.controller.Controller;
import cr.ac.una.sistemafichas.model.Branch;
import cr.ac.una.sistemafichas.model.Client;
import cr.ac.una.sistemafichas.model.CompanyConfig;
import cr.ac.una.sistemafichas.model.Procedure;
import cr.ac.una.sistemafichas.util.JsonUtil;
import io.github.palexdev.materialfx.controls.MFXTextField;
import io.github.palexdev.materialfx.utils.SwingFXUtils;
import java.awt.image.BufferedImage;
import java.io.File;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javax.imageio.ImageIO;

public class MaintenanceClientController extends Controller {

    // ──────────────── FXML ────────────────
    @FXML
    private ListView<Client> listClients;
    @FXML
    private MFXTextField txtName;
    @FXML
    private MFXTextField txtId;
    @FXML
    private MFXTextField txtAge;
    @FXML
    private ImageView imgFoto;
    @FXML
    private ImageView imgLogo;
    @FXML
    private Label lbCompany;

    // ──────────────── PATHS ────────────────
    private static final String CONFIG_PATH = "data/config.json";
    private static final String Clients_PATH = "data/clients.json";
    // ──────────────── DATA ────────────────
    private List<Client> client;
    private Client selectedClient;

    @Override
    public void initialize() {
        loadHeader();
        loadData();
        setupSelection();
    }

    private void loadData() {

        Type clientListType = new TypeToken<List<Client>>() {
        }.getType();
        client = JsonUtil.read(Clients_PATH, clientListType);
        if (client == null) {
            client = new ArrayList<>();
        }
        refreshClientes();
    }

    private void refreshClientes() {
        listClients.getItems().setAll(client);
    }

    private void setupSelection() {

        listClients.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            selectedClient = newVal;

            if (newVal != null) {
                txtName.setText(newVal.getName());

            }
        });
    }

    private void loadHeader() {
        CompanyConfig config = JsonUtil.read(CONFIG_PATH, CompanyConfig.class);
        if (config == null) {
            return;
        }

        lbCompany.setText(config.getCompanyName());
        try {
            File file = new File(config.getLogoPath());
            if (file.exists()) {
                imgLogo.setImage(new Image(file.toURI().toString()));
            }
        } catch (Exception e) {
            System.out.println("Error loading logo");
        }
    }

    @FXML
    private void OnActionBtnAddPhoto(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccionar Foto");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Imágenes", "*.png", "*.jpg", "*.jpeg", "*.gif")
        );

        File selectedFile = fileChooser.showOpenDialog(null);

        if (selectedFile == null) {
            return;
        }
        try {
            // Copia la imagen a data/images/
            File folder = new File("data/images");
            if(!folder.exists())folder.mkdirs();
            // Le da un nombre unico para cada imagen
            String fileName = "client "+System.currentTimeMillis()+".png";
            File destinationFile = new File(folder, fileName);
            
            
            BufferedImage buferedImage = ImageIO.read(selectedFile);
            
            ImageIO.write(buferedImage, "PNG" ,destinationFile);
            
            Image image = SwingFXUtils.toFXImage(buferedImage, null);
            imgFoto.setImage(image);
            
            if(selectedClient != null){
                selectedClient.setPhoto(destinationFile.getAbsolutePath());
            }   
        } catch (Exception e) {
          showAlert("Error al cargar la imagen");
        }

    }

    @FXML
    private void OnActionBtnAddClient(ActionEvent event) {
    }

    @FXML
    private void OnActionBtnDeleteClient(ActionEvent event) {
    }

    @FXML
    private void OnActionBtnTakephoto(ActionEvent event) {
    }

}

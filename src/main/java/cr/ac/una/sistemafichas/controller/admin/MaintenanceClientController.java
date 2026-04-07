package cr.ac.una.sistemafichas.controller.admin;

import com.google.gson.reflect.TypeToken;
import cr.ac.una.sistemafichas.controller.Controller;
import cr.ac.una.sistemafichas.model.Client;
import cr.ac.una.sistemafichas.model.CompanyConfig;
import cr.ac.una.sistemafichas.util.JsonUtil;
import io.github.palexdev.materialfx.controls.MFXTextField;
import java.awt.image.BufferedImage;
import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javax.imageio.ImageIO;

public class MaintenanceClientController extends Controller {

    // ──────────────── FXML ────────────────
    @FXML
    private ListView<Client> listClients;
    @FXML
    private MFXTextField txtClientName;
    @FXML
    private MFXTextField txtClientId;
    @FXML
    private MFXTextField txtClientAge;
    @FXML
    private ImageView imgFoto;

    // ──────────────── PATHS ────────────────
    private static final String CONFIG_PATH = "data/config.json";
    private static final String Clients_PATH = "data/clients.json";

    // ──────────────── DATA ────────────────
    private List<Client> client;
    private Client selectedClient;

    @Override
    public void initialize() {
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
        listClients.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue != null) {
                selectedClient = newValue;

                txtClientName.setText(newValue.getName());
                txtClientId.setText(newValue.getId());
                txtClientAge.setText(String.valueOf(newValue.getAge()));

                if (newValue.getPhoto() != null) {
                    File file = new File(newValue.getPhoto());
                    if (file.exists()) {
                        imgFoto.setImage(new Image(file.toURI().toString()));
                    } else {
                        imgFoto.setImage(null);
                    }
                } else {
                    imgFoto.setImage(null);
                }
            }
        });
    }

    private void clearClient() {
        txtClientName.clear();
        txtClientId.clear();
        txtClientAge.clear();
        imgFoto.setImage(null);
        selectedClient = null;
    }

    @FXML
    private void OnActionBtnAddClient(ActionEvent event) {
        String name = txtClientName.getText().trim();
        String id = txtClientId.getText().trim();
        String ageText = txtClientAge.getText().trim();

        if (name.isEmpty() || id.isEmpty() || ageText.isEmpty()) {
            showAlert("Complete todos los campos.");
            return;
        }
        int age;
        try {
            age = Integer.parseInt(ageText);

        } catch (Exception e) {
            showAlert("Edad Invalida");
            return;
        }
        for (Client c : client) {
            if (c.getId().equalsIgnoreCase(id)) {
                showAlert("El cliente ya existe");
                return;
            }
        }
        Client nuevo = new Client(name, id, age, null);
        client.add(nuevo);

        JsonUtil.write(Clients_PATH, client);
        refreshClientes();
        clearClient();
    }

    @FXML
    private void OnActionBtnDeleteClient(ActionEvent event) {
        if (selectedClient == null) {
            showAlert("Seleccione un cliente.");
            return;
        }

        client.remove(selectedClient);

        JsonUtil.write(Clients_PATH, client);
        refreshClientes();
        clearClient();

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
            if (!folder.exists()) {
                folder.mkdirs();
            }
            // Le da un nombre unico para cada imagen
            String fileName = "client " + System.currentTimeMillis() + ".png";
            File destinationFile = new File(folder, fileName);

            BufferedImage buferedImage = ImageIO.read(selectedFile);

            ImageIO.write(buferedImage, "PNG", destinationFile);

            Image image = SwingFXUtils.toFXImage(buferedImage, null);
            imgFoto.setImage(image);

            if (selectedClient != null) {
                selectedClient.setPhoto(destinationFile.getAbsolutePath());
            }
        } catch (Exception e) {
            showAlert("Error al cargar la imagen");
        }
    }

    @FXML
    private void OnActionBtnTakePhoto(ActionEvent event) {
    }
}

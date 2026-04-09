package cr.ac.una.sistemafichas.controller.admin;

import com.google.gson.reflect.TypeToken;
import cr.ac.una.sistemafichas.controller.Controller;
import cr.ac.una.sistemafichas.model.Client;
import cr.ac.una.sistemafichas.util.Formato;
import cr.ac.una.sistemafichas.util.JsonUtil;
import cr.ac.una.sistemafichas.util.Mensaje;
import io.github.palexdev.materialfx.controls.MFXTextField;
import java.awt.image.BufferedImage;
import java.io.File;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javax.imageio.ImageIO;
import cr.ac.una.sistemafichas.util.Validador;
import javafx.scene.Node;

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

    private List<Node> requeridos = new ArrayList();

    @Override
    public void initialize() {
        loadData();
        setupSelection();
        txtClientId.delegateSetTextFormatter(Formato.getInstance().integerFormat());
        txtClientAge.delegateSetTextFormatter(Formato.getInstance().integerFormat());
        txtClientName.delegateSetTextFormatter(Formato.getInstance().letrasFormat(30));

        requeridos.add(txtClientName);
        requeridos.add(txtClientId);
        requeridos.add(txtClientAge);
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
        String invalidos = Validador.validarRequeridos(requeridos);
        try {
            if (!invalidos.isBlank()) {
                new Mensaje().show(Alert.AlertType.INFORMATION, "Campos vacios", "Llene todos los campos");
                return;
            }
            String name = txtClientName.getText().trim();
            String id = txtClientId.getText().trim();
            String ageText = txtClientAge.getText().trim();
            int age = Integer.parseInt(ageText);

            for (Client c : client) {
                if (c.getId().equalsIgnoreCase(id)) {
                    new Mensaje().show(Alert.AlertType.INFORMATION, "Cliente existente", "El cliente ya existe.");
                    return;
                }
            }

            Client nuevo = new Client(name, id, age, null);
            client.add(nuevo);

            JsonUtil.write(Clients_PATH, client);
            refreshClientes();
            clearClient();
            new Mensaje().showModal(Alert.AlertType.INFORMATION, "Cliente agregado", getStage(), "El cliente se agregó correctamente.");

        } catch (Exception ex) {
            Logger.getLogger(MaintenanceClientController.class.getName()).log(Level.SEVERE, "Error agregando cliente", ex);
            new Mensaje().showModal(Alert.AlertType.ERROR, "Error", getStage(), "Ocurrió un error al guardar el cliente.");
        }
    }

    @FXML
    private void OnActionBtnDeleteClient(ActionEvent event
    ) {
        try {
            client.remove(selectedClient);
            if (this.selectedClient.getId() != null) {
                if (new Mensaje().showConfirmation("Eliminar cliente", getStage(), "¿Esta seguro que desea eliminar al cliente?")) {

                    client.remove(selectedClient);
                    JsonUtil.write(Clients_PATH, client);
                    refreshClientes();
                    clearClient();
                }
            } else {
                new Mensaje().showModal(Alert.AlertType.INFORMATION, "Eliminar Cliente", getStage(), "El cliente se elimino correctamente");
            }
        } catch (Exception ex) {
            if (selectedClient == null) {
                new Mensaje().show(Alert.AlertType.INFORMATION, "No se selecciono un cliente", "Seleccione un cliente");
            } else {
                Logger.getLogger(MaintenanceClientController.class.getName()).log(Level.SEVERE, "Error eliminando el cliente", ex);
                new Mensaje().showModal(Alert.AlertType.ERROR, "Eliminar Empleado", getStage(), "Ocurrio un error eliminando el cliente.");
            }
        }

    }

    @FXML
    private void OnActionBtnAddPhoto(ActionEvent event
    ) {
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
                selectedClient.setPhoto(fileName);
            }
        } catch (Exception e) {
            new Mensaje().show(Alert.AlertType.INFORMATION, "Error al cargar la imagen", "Hubo un error al cargar imagen");
        }
    }

    @FXML
    private void OnActionBtnTakePhoto(ActionEvent event
    ) {
    }
}

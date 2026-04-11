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
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javax.imageio.ImageIO;
import cr.ac.una.sistemafichas.util.Validador;

public class MaintenanceClientController extends Controller {

    @FXML private ListView<Client> listClients;
    @FXML private MFXTextField txtClientName;
    @FXML private MFXTextField txtClientId;
    @FXML private MFXTextField txtClientAge;
    @FXML private ImageView imgFoto;

    private static final String CONFIG_PATH  = "data/config.json";
    private static final String CLIENTS_PATH = "data/clients.json";

    private List<Client> client;
    private Client selectedClient;
    private List<Node> requeridos = new ArrayList<>();

    @Override
    public void initialize() {
        loadData();
        setupSelection();
        txtClientId.delegateSetTextFormatter(Formato.getInstance().integerFormat());
        txtClientName.delegateSetTextFormatter(Formato.getInstance().letrasFormat(30));

        requeridos.add(txtClientName);
        requeridos.add(txtClientId);
        requeridos.add(txtClientAge);
    }

    private void loadData() {
        Type clientListType = new TypeToken<List<Client>>(){}.getType();
        client = JsonUtil.read(CLIENTS_PATH, clientListType);
        if (client == null) client = new ArrayList<>();
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
                txtClientAge.setText(newValue.getAge()); // age ya es String

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
            String id   = txtClientId.getText().trim();
            String age  = txtClientAge.getText().trim(); // age es String directo

            for (Client c : client) {
                if (c.getId().equalsIgnoreCase(id)) {
                    new Mensaje().show(Alert.AlertType.INFORMATION, "Cliente existente", "El cliente ya existe.");
                    return;
                }
            }

            Client nuevo = new Client(name, id, age, null, false);
            client.add(nuevo);

            JsonUtil.write(CLIENTS_PATH, client);
            refreshClientes();
            clearClient();
            new Mensaje().showModal(Alert.AlertType.INFORMATION, "Cliente agregado", getStage(), "El cliente se agregó correctamente.");

        } catch (Exception ex) {
            Logger.getLogger(MaintenanceClientController.class.getName()).log(Level.SEVERE, "Error agregando cliente", ex);
            new Mensaje().showModal(Alert.AlertType.ERROR, "Error", getStage(), "Ocurrió un error al guardar el cliente.");
        }
    }

    @FXML
    private void OnActionBtnDeleteClient(ActionEvent event) {
        try {
            if (selectedClient == null) {
                new Mensaje().show(Alert.AlertType.INFORMATION, "No se selecciono un cliente", "Seleccione un cliente");
                return;
            }

            if (new Mensaje().showConfirmation("Eliminar cliente", getStage(), "¿Está seguro que desea eliminar al cliente?")) {
                client.remove(selectedClient);
                JsonUtil.write(CLIENTS_PATH, client);
                refreshClientes();
                clearClient();
                new Mensaje().showModal(Alert.AlertType.INFORMATION, "Eliminar Cliente", getStage(), "El cliente se eliminó correctamente.");
            }

        } catch (Exception ex) {
            Logger.getLogger(MaintenanceClientController.class.getName()).log(Level.SEVERE, "Error eliminando el cliente", ex);
            new Mensaje().showModal(Alert.AlertType.ERROR, "Eliminar Cliente", getStage(), "Ocurrió un error eliminando el cliente.");
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
        if (selectedFile == null) return;

        try {
            File folder = new File("data/images");
            if (!folder.exists()) folder.mkdirs();

            String fileName = "client_" + System.currentTimeMillis() + ".png";
            File destinationFile = new File(folder, fileName);

            BufferedImage bufferedImage = ImageIO.read(selectedFile);
            ImageIO.write(bufferedImage, "PNG", destinationFile);

            Image image = SwingFXUtils.toFXImage(bufferedImage, null);
            imgFoto.setImage(image);

            if (selectedClient != null) {
                selectedClient.setPhoto(destinationFile.getPath());
            }

        } catch (Exception ex) {
            Logger.getLogger(MaintenanceClientController.class.getName()).log(Level.SEVERE, "Error cargando la imagen", ex);
            new Mensaje().show(Alert.AlertType.INFORMATION, "Error al cargar la imagen", "Hubo un error al cargar la imagen.");
        }
    }

    @FXML
    private void OnActionBtnOpenCamera(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/cr/ac/una/sistemafichas/view/CameraView.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Camara");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception ex) {
            Logger.getLogger(MaintenanceClientController.class.getName()).log(Level.SEVERE, "Error cargando la camara", ex);
            new Mensaje().showModal(Alert.AlertType.INFORMATION, "Error al abrir la camara", getStage(), "Ocurrió un error al abrir la cámara.");
        }
    }
}
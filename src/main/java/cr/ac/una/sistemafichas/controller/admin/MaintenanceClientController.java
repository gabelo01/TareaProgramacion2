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

import io.github.palexdev.materialfx.controls.MFXCheckbox;
import java.time.LocalDate;
import java.time.Period;

public class MaintenanceClientController extends Controller {

    @FXML
    private ListView<Client> listClients;
    @FXML
    private MFXTextField txtClientName;
    @FXML
    private MFXTextField txtClientId;
    @FXML
    private MFXTextField txtClientAge;
    @FXML
    private ImageView imgClient;
    @FXML
    private MFXCheckbox chkPreferencial;

    private static final String CLIENTS_PATH = "data/clients.json";

    private List<Client> client;
    private Client selectedClient;
    private List<Node> requeridos = new ArrayList<>();
    private String tempPhotoPath;

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
        Type clientListType = new TypeToken<List<Client>>() {
        }.getType();
        client = JsonUtil.read(CLIENTS_PATH, clientListType);
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
                txtClientAge.setText(newValue.getAge());

                if (newValue.getPhoto() != null) {
                    File file = new File(newValue.getPhoto());
                    if (file.exists()) {
                        imgClient.setImage(new Image(file.toURI().toString()));
                    } else {
                        imgClient.setImage(null);
                    }
                } else {
                    imgClient.setImage(null);
                }

                int age = calculateAge(newValue.getAge());

                if (age >= 65) {
                    chkPreferencial.setSelected(true);
                    chkPreferencial.setDisable(true);
                } else {
                    chkPreferencial.setDisable(false);
                    chkPreferencial.setSelected(newValue.isPreferential());
                }

            } else {
                clearClient();
            }
        });
    }

    private void clearClient() {
        txtClientName.clear();
        txtClientId.clear();
        txtClientAge.clear();
        imgClient.setImage(null);

        selectedClient = null;
        tempPhotoPath = null;

        chkPreferencial.setSelected(false);
        chkPreferencial.setDisable(false);
    }

    @FXML
    private void OnActionBtnAddClient(ActionEvent event) {

        String invalidos = Validador.validarRequeridos(requeridos);

        if (!invalidos.isBlank()) {
            new Mensaje().show(Alert.AlertType.INFORMATION, "Empty fields", "Please fill all fields");
            return;
        }

        String name = txtClientName.getText().trim();
        String id = txtClientId.getText().trim();
        String birthDate = txtClientAge.getText().trim();

        // duplicate check
        for (Client c : client) {
            if (c.getId().equalsIgnoreCase(id)) {
                new Mensaje().show(Alert.AlertType.INFORMATION, "Client exists", "Client already exists.");
                return;
            }
        }

        // calculate age
        int age = 0;
        try {
            LocalDate date = LocalDate.parse(birthDate);
            age = Period.between(date, LocalDate.now()).getYears();
        } catch (Exception e) {
            new Mensaje().show(Alert.AlertType.INFORMATION, "Invalid date", "Use format YYYY-MM-DD");
            return;
        }

        boolean isPreferential = (age >= 65) || chkPreferencial.isSelected();

        Client newClient = new Client(name, id, birthDate, tempPhotoPath, isPreferential);

        client.add(newClient);
        JsonUtil.write(CLIENTS_PATH, client);

        refreshClientes();
        clearClient();

        tempPhotoPath = null;

        new Mensaje().showModal(Alert.AlertType.INFORMATION, "Client added", getStage(), "Success");
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
        if (selectedFile == null) {
            return;
        }

        try {
            File folder = new File("data/fotos-clientes");
            if (!folder.exists()) {
                folder.mkdirs();
            }

            String fileName = "client_" + System.currentTimeMillis() + ".png";
            File destinationFile = new File(folder, fileName);

            BufferedImage bufferedImage = ImageIO.read(selectedFile);
            ImageIO.write(bufferedImage, "PNG", destinationFile);

            Image image = SwingFXUtils.toFXImage(bufferedImage, null);
            imgClient.setImage(image);

            tempPhotoPath = destinationFile.getPath(); // guardar temporal

            if (selectedClient != null) {   //guardar directo si esta editando
                selectedClient.setPhoto(tempPhotoPath);

                JsonUtil.write(CLIENTS_PATH, client);

                new Mensaje().show(Alert.AlertType.INFORMATION, "Foto guardada", "Se actualizó la foto del cliente");
            }

        } catch (Exception ex) {
            Logger.getLogger(MaintenanceClientController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void OnActionBtnEditClient(ActionEvent event) {

        try {
            if (selectedClient == null) {
                new Mensaje().show(Alert.AlertType.INFORMATION, "No selection", "Select a client to edit.");
                return;
            }

            String invalidos = Validador.validarRequeridos(requeridos);
            if (!invalidos.isBlank()) {
                new Mensaje().show(Alert.AlertType.INFORMATION, "Empty fields", "Please fill all fields.");
                return;
            }

            String name = txtClientName.getText().trim();
            String id = txtClientId.getText().trim();
            String birthDate = txtClientAge.getText().trim();

            // duplicate ID check
            for (Client c : client) {
                if (c.getId().equalsIgnoreCase(id)
                        && !c.getId().equalsIgnoreCase(selectedClient.getId())) {
                    new Mensaje().show(Alert.AlertType.INFORMATION, "Duplicate ID", "Another client already uses this ID.");
                    return;
                }
            }

            // calculate age
            int age = 0;
            try {
                LocalDate date = LocalDate.parse(birthDate);
                age = Period.between(date, LocalDate.now()).getYears();
            } catch (Exception e) {
                new Mensaje().show(Alert.AlertType.INFORMATION, "Invalid date", "Use format YYYY-MM-DD");
                return;
            }

            boolean isPreferential = (age >= 65) || chkPreferencial.isSelected();

            // update client
            selectedClient.setName(name);
            selectedClient.setId(id);
            selectedClient.setAge(birthDate);
            selectedClient.setPreferential(isPreferential);

            if (tempPhotoPath != null) {
                selectedClient.setPhoto(tempPhotoPath);
            }

            JsonUtil.write(CLIENTS_PATH, client);
            refreshClientes();
            clearClient();

            new Mensaje().showModal(Alert.AlertType.INFORMATION,
                    "Client updated",
                    getStage(),
                    "Updated successfully");

        } catch (Exception ex) {
            Logger.getLogger(MaintenanceClientController.class.getName())
                    .log(Level.SEVERE, null, ex);
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

    @FXML
    private void onActionBtnNuevo(ActionEvent event) {
        clearClient();
    }

    @FXML
    private void OnActionBtnScanID(ActionEvent event) {

        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/cr/ac/una/sistemafichas/view/admin/CameraIdView.fxml")
            );

            Parent root = loader.load();

            CameraIdController controller = loader.getController();

            controller.setOnDataCaptured((id, name) -> {
                txtClientId.setText(id);
            });

            Stage stage = new Stage();
            stage.setTitle("Escanear Cedula");
            stage.setScene(new Scene(root));
            stage.show();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private int calculateAge(String birthDateText) {
        try {
            LocalDate birthDate = LocalDate.parse(birthDateText);
            return Period.between(birthDate, LocalDate.now()).getYears();
        } catch (Exception e) {
            return 0;
        }
    }
}

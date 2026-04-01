package cr.ac.una.sistemafichas.controller.admin;

import com.google.gson.reflect.TypeToken;
import cr.ac.una.sistemafichas.controller.Controller;
import cr.ac.una.sistemafichas.model.Branch;
import cr.ac.una.sistemafichas.model.Client;
import cr.ac.una.sistemafichas.model.CompanyConfig;
import cr.ac.una.sistemafichas.model.Procedure;
import cr.ac.una.sistemafichas.util.JsonUtil;
import io.github.palexdev.materialfx.controls.MFXTextField;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

public class MaintenanceClientController extends Controller {

    @FXML
    private ListView<Client> listClientes;
    @FXML
    private MFXTextField txtNombre;
    @FXML
    private MFXTextField txtId;
    @FXML
    private MFXTextField txtEdad;
    @FXML
    private ImageView imgFoto;

    private static final String Clients_PATH = "data/clients.json";
    
    private List<Client> client;
    private Client selectedClient;
    
    @Override
    public void initialize() {
        loadData();
        setupSelection();
    }
    
    private void loadData() {

        Type clientListType = new TypeToken<List<Client>>(){}.getType();
           client = JsonUtil.read(Clients_PATH, clientListType);
           if (client == null) client = new ArrayList<>();
        refreshClientes();
       }

     private void refreshClientes() {
        listClientes.getItems().setAll(client);
     }

      private void setupSelection() {

         listClientes.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            selectedClient = newVal;

            if (newVal != null) {
                txtNombre.setText(newVal.getName());
              
            }
        });
    }

    @FXML
    private void OnActionBtnAddPhoto(ActionEvent event) {
        
    }
    
    @FXML
    private void OnActionBtnAddClient(ActionEvent event) {
    }

    @FXML
    private void OnActionBtnDeleteClient(ActionEvent event) {
    }

    
    
}

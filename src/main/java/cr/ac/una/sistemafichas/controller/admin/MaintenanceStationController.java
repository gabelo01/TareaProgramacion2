package cr.ac.una.sistemafichas.controller.admin;

import com.google.gson.reflect.TypeToken;
import cr.ac.una.sistemafichas.controller.Controller;
import cr.ac.una.sistemafichas.model.Procedure;
import cr.ac.una.sistemafichas.model.Station;
import cr.ac.una.sistemafichas.util.JsonUtil;
import cr.ac.una.sistemafichas.util.Mensaje;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;

/**
 * FXML Controller class
 *
 * @author diazv
 */
public class MaintenanceStationController extends Controller {

    @FXML
    private TextField txtNombre;
    @FXML
    private CheckBox chkPreferencial;
    @FXML
    private ListView<Procedure> tblListAvailableProcedures;
    @FXML
    private ListView<Procedure> tblListAsignedProcedures;
    @FXML
    private ListView<Station> tblListStations;

    private static final String STATION_PATH = "data/station.json";
    private static final String PROCEDURES_PATH = "data/procedures.json";

    private List<Procedure> allProcedures = new ArrayList<>();
    private List<Station> stations;
    private Station selectedStation;
    @FXML
    private Label label;

    @Override
    public void initialize() {
        loadData();
        setupSelection();
        loadProcedures();

        tblListAvailableProcedures.getItems().setAll(allProcedures);
        dragAnddropConfiguration();
    }

    private void loadData() {
        Type stationlistType = new TypeToken<List<Station>>() {
        }.getType();
        stations = JsonUtil.read(STATION_PATH, stationlistType);
        if (stations == null) {
            stations = new ArrayList<>();
        }

        refreshStation();

    }

    private void refreshStation() {
        tblListStations.getItems().setAll(stations);
    }

    private void setupSelection() {
        tblListStations.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            selectedStation = newVal;
            if (newVal != null) {
                txtNombre.setText(newVal.getName());
                chkPreferencial.setSelected(newVal.isPreferential());
            }
        });
    }

    private void loadProcedures() {
        Type listType = new TypeToken<List<Procedure>>() {
        }.getType();
        allProcedures = JsonUtil.read(PROCEDURES_PATH, listType);

        if (allProcedures == null) {
            allProcedures = new ArrayList<>();
        }
    }

    @FXML
    private void onActionNuevo(ActionEvent event) {
        txtNombre.clear();
        chkPreferencial.setSelected(false);

        tblListAsignedProcedures.getItems().clear();
        tblListAvailableProcedures.getItems().setAll(allProcedures);
    }

    private void aplicarPreferencial() {
        if (selectedStation != null) {
            selectedStation.setPreferential(chkPreferencial.isSelected());
        }
    }

    @FXML
    private void onActionGuardar(ActionEvent event) {
        try {
            String name = txtNombre.getText().trim();

            if (name.isEmpty()) {
                new Mensaje().show(Alert.AlertType.INFORMATION, "Espacio vacio", "Agregue un nombre.");
                return;
            }
            for (Station s : stations) {
                if (s.getName().equalsIgnoreCase(name)) {
                    new Mensaje().show(Alert.AlertType.INFORMATION, "Estacion existente", "Ya existe una estacion con el mismo nombre.");
                    return;
                }
            }
            if (selectedStation == null) {
                Station s = new Station(name, chkPreferencial.isSelected(), "", new ArrayList<>());
                stations.add(s);
            } else {
                selectedStation.setName(name);
                aplicarPreferencial();
            }
            JsonUtil.write(STATION_PATH, stations);
            tblListAvailableProcedures.getItems().setAll(allProcedures);
            tblListAsignedProcedures.getItems().clear();
            refreshStation();
            clearStation();
            new Mensaje().show(Alert.AlertType.INFORMATION, "Estacion Agregada", "Estacion agregada correctamente.");

        } catch (Exception e) {
            Logger.getLogger(MaintenanceStationController.class.getName()).log(Level.SEVERE, "Error agregando la estacion.");
            new Mensaje().show(Alert.AlertType.INFORMATION, "Error al agregar la estacion", "Ocurrio un error al agregar la estacion.");
        }
    }

    @FXML
    private void onActionEliminar(ActionEvent event) {
        try {
            if (selectedStation == null) {
                new Mensaje().show(Alert.AlertType.INFORMATION, "No se selecciono una estacion", "Seleccione una estacion.");
                return;
            }
            if (new Mensaje().showConfirmation("Eliminar Estacion", getStage(), "¿Seguro que desea eliminar esta estacion?")) {
                stations.remove(selectedStation);
                JsonUtil.write(STATION_PATH, stations);
                refreshStation();
                clearStation();
                new Mensaje().show(Alert.AlertType.INFORMATION, "Estacion eliminada", "La estacion se elimino correctamente");
            }
        } catch (Exception e) {
            Logger.getLogger(MaintenanceStationController.class.getName()).log(Level.SEVERE, "Error eliminando el cliente.");
            new Mensaje().showModal(Alert.AlertType.INFORMATION, "Error al eliminar el cliente", getStage(), "Ocurrio un error al eliminar el cliente");
        }
    }

    private void clearStation() {
        txtNombre.clear();
        chkPreferencial.setSelected(false);
        selectedStation = null;
    }

    private Procedure buscar(String name) {
        for (Procedure p : allProcedures) {
            if (p.getName().equals(name)) {
                return p;
            }
        }
        return null;
    }

    private void dragAnddropConfiguration() {
        // drag desde Tramites Disponibles
        tblListAvailableProcedures.setOnDragDetected(event -> {
            Procedure p = tblListAvailableProcedures.getSelectionModel().getSelectedItem();
            if (p == null) {
                new Mensaje().show(Alert.AlertType.INFORMATION, "Tramite inexistente", "Selecione otro tramite.");
                return;
            } else {
                Dragboard db = tblListAvailableProcedures.startDragAndDrop(TransferMode.ANY);

                ClipboardContent content = new ClipboardContent();
                content.putString(p.getName());
                db.setContent(content);
                Label label = new Label(p.getName());
                db.setDragView(label.snapshot(null, null));
            }
            event.consume();
        });
        // drop a Tramites Asignados
        tblListAsignedProcedures.setOnDragOver(event -> {

            if (event.getGestureSource() != tblListAsignedProcedures
                    && event.getDragboard().hasString()) {
                event.acceptTransferModes(TransferMode.MOVE);
            }
            event.consume();
        });
        tblListAsignedProcedures.setOnDragDropped(event -> {
            Procedure procedure = buscar(event.getDragboard().getString());
            if (procedure != null && !tblListAsignedProcedures.getItems().contains(procedure)) {

                tblListAsignedProcedures.getItems().add(procedure);
                tblListAvailableProcedures.getItems().remove(procedure);
            }
            event.setDropCompleted(true);
            event.consume();
        });

        // drag desde Tramites Asignados
        tblListAsignedProcedures.setOnDragDetected(event -> {
            Procedure p = tblListAsignedProcedures.getSelectionModel().getSelectedItem();
            if (p == null) {
                new Mensaje().show(Alert.AlertType.INFORMATION, "Tramite inexistente", "Selecione otro tramite.");
                return;
            } else {
                Dragboard db = tblListAsignedProcedures.startDragAndDrop(TransferMode.ANY);

                ClipboardContent content = new ClipboardContent();
                content.putString(p.getName());
                db.setContent(content);
                Label label = new Label(p.getName());
                db.setDragView(label.snapshot(null, null));
            }
            event.consume();
        });
        // drop a Tramites Asignados
        tblListAvailableProcedures.setOnDragOver(event -> {

            if (event.getGestureSource() != tblListAvailableProcedures
                    && event.getDragboard().hasString()) {
                event.acceptTransferModes(TransferMode.MOVE);
            }
            event.consume();
        });
        tblListAvailableProcedures.setOnDragDropped(event -> {
            Procedure procedure = buscar(event.getDragboard().getString());
            if (procedure != null && !tblListAvailableProcedures.getItems().contains(procedure)) {

                tblListAvailableProcedures.getItems().add(procedure);
                tblListAsignedProcedures.getItems().remove(procedure);
            }
            event.setDropCompleted(true);
            event.consume();
        });
    }

}

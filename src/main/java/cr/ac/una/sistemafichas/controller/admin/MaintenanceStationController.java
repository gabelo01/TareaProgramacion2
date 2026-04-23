package cr.ac.una.sistemafichas.controller.admin;

import com.google.gson.reflect.TypeToken;
import cr.ac.una.sistemafichas.controller.Controller;
import cr.ac.una.sistemafichas.model.Branch;
import cr.ac.una.sistemafichas.model.Procedure;
import cr.ac.una.sistemafichas.model.Station;
import cr.ac.una.sistemafichas.util.JsonUtil;
import cr.ac.una.sistemafichas.util.Mensaje;
import io.github.palexdev.materialfx.controls.MFXTextField;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.*;

public class MaintenanceStationController extends Controller {

    @FXML
    private MFXTextField txtName;
    @FXML 
    private CheckBox chkPreferential;
    @FXML
    private CheckBox chkIsStationActive;
    @FXML 
    private ComboBox<String> cmbBranches;

    @FXML 
    private ListView<Procedure> tblListAvailableProcedures;
    @FXML
    private ListView<Procedure> tblListAsignedProcedures;
    @FXML 
    private ListView<Station> tblListStations;

    private static final String BRANCH_PATH = "branches.json";
    private static final String PROCEDURES_PATH = "procedures.json";

    private List<Procedure> allProcedures = new ArrayList<>();
    private List<Branch> branches = new ArrayList<>();
    private List<Station> stations = new ArrayList<>();

    private Station selectedStation;
    private Branch currentBranch;

    @Override
    public void initialize() {
        loadBranches();
        loadProcedures();
        setupSelection();
        dragAndDrop();
    }

    private void loadBranches() {
        Type type = new TypeToken<List<Branch>>(){}.getType();
        branches = JsonUtil.read(BRANCH_PATH, type);

        if (branches == null) branches = new ArrayList<>();

        cmbBranches.getItems().setAll(
                branches.stream().map(Branch::getName).toList()
        );

        cmbBranches.setOnAction(e -> {
            String name = cmbBranches.getValue();

            currentBranch = branches.stream()
                    .filter(b -> b.getName().equals(name))
                    .findFirst()
                    .orElse(null);

            if (currentBranch != null) {
                 stations = currentBranch.getStations();
                tblListStations.getItems().setAll(stations);
            }
        });
    }

    private void loadProcedures() {
        Type type = new TypeToken<List<Procedure>>(){}.getType();
        allProcedures = JsonUtil.read(PROCEDURES_PATH, type);

        if (allProcedures == null) allProcedures = new ArrayList<>();

        allProcedures = allProcedures.stream()
                .filter(Procedure::isActive)
                .toList();

        tblListAvailableProcedures.getItems().setAll(allProcedures);
    }

    private void setupSelection() {
        tblListStations.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, st) -> {

            selectedStation = st;

            if (st != null) {
                txtName.setText(st.getName());
                chkPreferential.setSelected(st.isPreferential());
                chkIsStationActive.setSelected(st.isActive());

                List<Procedure> assigned = allProcedures.stream()
                        .filter(p -> st.getProcedureNames().contains(p.getName()))
                        .toList();

                List<Procedure> available = allProcedures.stream()
                        .filter(p -> !st.getProcedureNames().contains(p.getName()))
                        .toList();

                tblListAsignedProcedures.getItems().setAll(assigned);
                tblListAvailableProcedures.getItems().setAll(available);
            }
        });
    }

    @FXML
    private void onActionBtnNew(ActionEvent e) {
        txtName.clear();
        chkPreferential.setSelected(false);
        chkIsStationActive.setSelected(true);

        selectedStation = null;

        tblListAsignedProcedures.getItems().clear();
        tblListAvailableProcedures.getItems().setAll(allProcedures);
    }

    @FXML
    private void onActionBtnSave(ActionEvent e) {

        if (currentBranch == null) {
            new Mensaje().show(Alert.AlertType.INFORMATION, "Error", "Seleccione sucursal");
            return;
        }

        String name = txtName.getText().trim();

        if (name.isEmpty()) {
            new Mensaje().show(Alert.AlertType.INFORMATION, "Error", "Nombre requerido");
            return;
        }

        //si la estación esta activa debe tener un trámite minimo
        if (chkIsStationActive.isSelected() && tblListAsignedProcedures.getItems().isEmpty()) {
            new Mensaje().show(Alert.AlertType.WARNING, "Error","Una estación activa debe tener al menos un trámite asignado.");
            return;
        }

        List<String> procedures = tblListAsignedProcedures.getItems().stream().map(Procedure::getName).toList();

        if (selectedStation == null) {
            Station s = new Station(
                    name,
                    chkPreferential.isSelected(),
                    currentBranch.getName(),
                    procedures,
                    chkIsStationActive.isSelected()
            );
            stations.add(s);
        } else {// actualiza todo
            
            selectedStation.setName(name);
            selectedStation.setPreferential(chkPreferential.isSelected());
            selectedStation.setProcedureNames(procedures);
            selectedStation.setActive(chkIsStationActive.isSelected());
    }

    JsonUtil.write(BRANCH_PATH, branches);

    tblListStations.getItems().setAll(stations);
    onActionBtnNew(null);

    new Mensaje().show(Alert.AlertType.INFORMATION, "OK", "Guardado");
}

    @FXML
    private void onActionBtnDelete(ActionEvent event) {
        if (selectedStation == null) return;

        stations.remove(selectedStation);
        JsonUtil.write(BRANCH_PATH, branches);

        tblListStations.getItems().setAll(stations);
        onActionBtnNew(null);
    }

    private Procedure buscar(String name) {
        return allProcedures.stream()
                .filter(p -> p.getName().equals(name))
                .findFirst()
                .orElse(null);
    }

    private void dragAndDrop() {

        tblListAvailableProcedures.setOnDragDetected(e -> {
            Procedure p = tblListAvailableProcedures.getSelectionModel().getSelectedItem();
            if (p == null) return;

            Dragboard db = tblListAvailableProcedures.startDragAndDrop(TransferMode.MOVE);
            ClipboardContent cc = new ClipboardContent();
            cc.putString(p.getName());
            db.setContent(cc);
        });

        tblListAsignedProcedures.setOnDragOver(e -> {
            if (e.getDragboard().hasString()) e.acceptTransferModes(TransferMode.MOVE);
        });

        tblListAsignedProcedures.setOnDragDropped(e -> {
            Procedure p = buscar(e.getDragboard().getString());

            if (p != null && !tblListAsignedProcedures.getItems().contains(p)) {
                tblListAsignedProcedures.getItems().add(p);
                tblListAvailableProcedures.getItems().remove(p);
            }
        });

        tblListAsignedProcedures.setOnDragDetected(e -> {
            Procedure p = tblListAsignedProcedures.getSelectionModel().getSelectedItem();
            if (p == null) return;

            Dragboard db = tblListAsignedProcedures.startDragAndDrop(TransferMode.MOVE);
            ClipboardContent cc = new ClipboardContent();
            cc.putString(p.getName());
            db.setContent(cc);
        });

        tblListAvailableProcedures.setOnDragOver(e -> {
            if (e.getDragboard().hasString()) e.acceptTransferModes(TransferMode.MOVE);
        });

        tblListAvailableProcedures.setOnDragDropped(e -> {
            Procedure p = buscar(e.getDragboard().getString());

            if (p != null && !tblListAvailableProcedures.getItems().contains(p)) {
                tblListAvailableProcedures.getItems().add(p);
                tblListAsignedProcedures.getItems().remove(p);
            }
        });
    }
}
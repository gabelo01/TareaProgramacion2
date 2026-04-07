package cr.ac.una.sistemafichas.controller.admin;

import cr.ac.una.sistemafichas.controller.Controller;
import cr.ac.una.sistemafichas.model.CompanyConfig;
import cr.ac.una.sistemafichas.model.Procedure;
import cr.ac.una.sistemafichas.util.FlowController;
import cr.ac.una.sistemafichas.util.JsonUtil;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import com.google.gson.reflect.TypeToken;
import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ProceduresMaintenanceController extends Controller {

    // ──────────────── FXML ────────────────

    @FXML private MFXTextField txtProcedureName;
    @FXML private CheckBox chkActiveProcedure;
    @FXML private ListView<Procedure> listProcedures;

    // ──────────────── PATHS ────────────────

    private static final String PROCEDURES_PATH = "data/procedures.json";
    private static final String CONFIG_PATH = "data/config.json";

    // ──────────────── DATA ────────────────

    private List<Procedure> procedures;
    private Procedure selectedProcedure;

    // ──────────────── INIT ────────────────

    @Override
    public void initialize() {
        loadData();
        setupSelection();
    }

    private void loadData() {
        Type procedureListType = new TypeToken<List<Procedure>>(){}.getType();
        procedures = JsonUtil.read(PROCEDURES_PATH, procedureListType);
        if (procedures == null) procedures = new ArrayList<>();
        refreshProcedures();
    }

    private void refreshProcedures() {
        listProcedures.getItems().setAll(procedures);
    }

    // ──────────────── SELECTION ────────────────

    private void setupSelection() {
        listProcedures.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            selectedProcedure = newVal;
            if (newVal != null) {
                txtProcedureName.setText(newVal.getName());
                chkActiveProcedure.setSelected(newVal.isActive());
            }
        });
    }

    // ──────────────── ACTIONS ────────────────

    @FXML
    private void btnAddProcedure() {
        String name = txtProcedureName.getText().trim();

        if (name.isEmpty()) {
            showAlert("Ingrese un nombre de trámite.");
            return;
        }

        for (Procedure p : procedures) {
            if (p.getName().equalsIgnoreCase(name)) {
                showAlert("El trámite ya existe.");
                return;
            }
        }

        Procedure p = new Procedure(name, chkActiveProcedure.isSelected());
        procedures.add(p);
        JsonUtil.write(PROCEDURES_PATH, procedures);
        refreshProcedures();
        clearProcedure();
    }

    @FXML
    private void btnDeleteProcedure() {
        if (selectedProcedure == null) {
            showAlert("Seleccione un trámite.");
            return;
        }

        procedures.remove(selectedProcedure);
        JsonUtil.write(PROCEDURES_PATH, procedures);
        refreshProcedures();
        clearProcedure();
    }

    private void clearProcedure() {
        txtProcedureName.clear();
        chkActiveProcedure.setSelected(false);
        selectedProcedure = null;
    }

    @FXML
    private void btnBack() {
       FlowController.getInstance().goViewReplace("admin/SelectMaintenance");
    }

}

package cr.ac.una.sistemafichas.controller;

import cr.ac.una.sistemafichas.model.CompanyConfig;
import cr.ac.una.sistemafichas.model.Procedure;
import cr.ac.una.sistemafichas.model.Branch;
import cr.ac.una.sistemafichas.util.FlowController;
import cr.ac.una.sistemafichas.util.JsonUtil;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ProceduresAndBranchMaintenanceController extends Controller {

    // ──────────────── FXML ────────────────

    @FXML private MFXTextField txtProcedureName;
    @FXML private CheckBox chkActiveProcedure;
    @FXML private ListView<Procedure> listProcedures;

    @FXML private MFXTextField txtBranchName;
    @FXML private CheckBox chkActiveBranch;
    @FXML private ListView<Branch> listProcedures1; // (sí, tu fx:id es ese)

    @FXML private Label lbCompany;
    @FXML private ImageView imgLogo;

    // ──────────────── PATHS ────────────────

    private static final String PROCEDURES_PATH = "data/procedures.json";
    private static final String BRANCHES_PATH = "data/branches.json";
    private static final String CONFIG_PATH = "data/config.json";

    // ──────────────── DATA ────────────────

    private List<Procedure> procedures;
    private List<Branch> branches;

    private Procedure selectedProcedure;
    private Branch selectedBranch;

    // ──────────────── INIT ────────────────

    @Override
    public void initialize() {
        loadHeader();
        loadData();
        setupSelection();
    }

    // ──────────────── LOAD DATA ────────────────

    private void loadData() {

        // 🔹 procedimientos
        procedures = JsonUtil.read(PROCEDURES_PATH, List.class);
        if (procedures == null) {
            procedures = new ArrayList<>();
        }

        // 🔹 sucursales
        branches = JsonUtil.read(BRANCHES_PATH, List.class);
        if (branches == null) {
            branches = new ArrayList<>();
        }

        refreshProcedures();
        refreshBranches();
    }

    private void refreshProcedures() {
        listProcedures.getItems().setAll(procedures);
    }

    private void refreshBranches() {
        listProcedures1.getItems().setAll(branches);
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

        listProcedures1.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            selectedBranch = newVal;

            if (newVal != null) {
                txtBranchName.setText(newVal.getName());
            }
        });
    }

    // ──────────────── PROCEDURES ────────────────

    @FXML
    private void onAddProcedure() {
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
    private void onDeleteProcedure() {
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

    // ──────────────── BRANCHES ────────────────

    @FXML
    private void onAdd() {
        String name = txtBranchName.getText().trim();

        if (name.isEmpty()) {
            showAlert("Ingrese un nombre de sucursal.");
            return;
        }

        for (Branch b : branches) {
            if (b.getName().equalsIgnoreCase(name)) {
                showAlert("La sucursal ya existe.");
                return;
            }
        }

        Branch b = new Branch(name, "", "", new ArrayList<>());
        branches.add(b);

        JsonUtil.write(BRANCHES_PATH, branches);
        refreshBranches();
        clearBranch();
    }

    @FXML
    private void onDeleteBranch() {
        if (selectedBranch == null) {
            showAlert("Seleccione una sucursal.");
            return;
        }

        branches.remove(selectedBranch);
        JsonUtil.write(BRANCHES_PATH, branches);
        refreshBranches();
        clearBranch();
    }

    private void clearBranch() {
        txtBranchName.clear();
        selectedBranch = null;
    }

    // ──────────────── GENERAL ────────────────

    @FXML
    private void onUpdate() {
        JsonUtil.write(PROCEDURES_PATH, procedures);
        JsonUtil.write(BRANCHES_PATH, branches);
        showAlert("Datos actualizados.");
    }

    @FXML
    private void onBack() {
        FlowController.getInstance().goView("SelectMaintenance");
    }

    private void showAlert(String msg) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setHeaderText(null);
        a.setContentText(msg);
        a.showAndWait();
    }

    private void loadHeader() {
        CompanyConfig config = JsonUtil.read(CONFIG_PATH, CompanyConfig.class);
        if (config == null) return;

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
}
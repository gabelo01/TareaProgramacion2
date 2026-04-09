package cr.ac.una.sistemafichas.controller.admin;

import cr.ac.una.sistemafichas.controller.Controller;
import cr.ac.una.sistemafichas.model.Branch;
import cr.ac.una.sistemafichas.model.CompanyConfig;
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
import cr.ac.una.sistemafichas.util.Mensaje;
import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.Alert;

public class BranchMaintenanceController extends Controller {

    // ──────────────── FXML ────────────────
    @FXML
    private MFXTextField txtBranchName;
    @FXML
    private CheckBox chkActiveBranch;
    @FXML
    private ListView<Branch> listBranches;

    // ──────────────── PATHS ────────────────
    private static final String BRANCHES_PATH = "data/branches.json";
    private static final String CONFIG_PATH = "data/config.json";

    // ──────────────── DATA ────────────────
    private List<Branch> branches;
    private Branch selectedBranch;

    // ──────────────── INIT ────────────────
    @Override
    public void initialize() {
        loadData();
        setupSelection();
        chkActiveBranch.selectedProperty().addListener((obs, oldVal, newVal) -> {
            if (selectedBranch != null) {
                selectedBranch.setActive(newVal);
                JsonUtil.write(BRANCHES_PATH, branches);
            }
        });
    }

    private void loadData() {
        Type branchListType = new TypeToken<List<Branch>>() {
        }.getType();
        branches = JsonUtil.read(BRANCHES_PATH, branchListType);
        if (branches == null) {
            branches = new ArrayList<>();
        }
        refreshBranches();
    }

    private void refreshBranches() {
        listBranches.getItems().setAll(branches);
    }

    // ──────────────── SELECTION ────────────────
    private void setupSelection() {
        listBranches.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            selectedBranch = newVal;
            if (newVal != null) {
                txtBranchName.setText(newVal.getName());
            }
        });
    }

    // ──────────────── ACTIONS ────────────────
    @FXML
    private void btnAddBranch() {
        try {
            String name = txtBranchName.getText().trim();

            if (name.isEmpty()) {
                new Mensaje().show(Alert.AlertType.INFORMATION, "Nombre vacio", "Ingrese un nombre de sucursal.");
                return;
            }

            for (Branch b : branches) {
                if (b.getName().equalsIgnoreCase(name)) {
                    new Mensaje().show(Alert.AlertType.INFORMATION, "Nombre de sucursal exixtente", "La sucursal ya existe.");
                    return;
                }
            }

            Branch b = new Branch(name, "", "", new ArrayList<>(), true);
            branches.add(b);
            JsonUtil.write(BRANCHES_PATH, branches);
            refreshBranches();
            clearBranch();
            new Mensaje().showModal(Alert.AlertType.INFORMATION, "Sucursal guardada", getStage(), "La sucursal se guardo correctamente");

        } catch (Exception e) {
            Logger.getLogger(ProceduresMaintenanceController.class.getName()).log(Level.SEVERE, "Error agregando la sucursal", e);
            new Mensaje().showModal(Alert.AlertType.ERROR, "Error agregando la sucursal", getStage(), "Hubo un error al agregar la sucursal.");
        }
    }

    @FXML
    private void btnDeleteBranch() {
        try {
            if (selectedBranch == null) {
                new Mensaje().show(Alert.AlertType.INFORMATION, "No se selecciono una sucursal", "Seleccione una sucursal.");
                return;
            }
            if (new Mensaje().showConfirmation("Eliminar Tramite", getStage(), "¿Esta seguro que desea eliminar el cliente?")) {
                branches.remove(selectedBranch);
                JsonUtil.write(BRANCHES_PATH, branches);
                refreshBranches();
                clearBranch();
                new Mensaje().showModal(Alert.AlertType.INFORMATION, "Se elimino la sucursal", getStage(), "Se elimino la sucursal correctamente.");
            }
        } catch (Exception ex) {
            Logger.getLogger(ProceduresMaintenanceController.class.getName()).log(Level.SEVERE, "Error eliminando la sucursal", ex);
            new Mensaje().showModal(Alert.AlertType.ERROR, "Error eliminando la sucursal", getStage(), "Hubo un error al eliminar la sucursal.");
        }

    }

    private void clearBranch() {
        txtBranchName.clear();
        chkActiveBranch.setSelected(false);
        selectedBranch = null;
    }

    @FXML
    private void btnBack() {
        FlowController.getInstance().goViewReplace("admin/SelectMaintenance");
    }

}
